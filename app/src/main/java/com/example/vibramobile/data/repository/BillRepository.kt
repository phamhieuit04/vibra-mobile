package com.example.vibramobile.data.repository

import android.content.Context
import com.example.vibramobile.domain.contract.IBillRepository
import com.example.vibramobile.domain.model.Bill
import com.example.vibramobile.data.source.remote.dto.BillResponseDto
import com.example.vibramobile.data.source.remote.dto.SongResponseDto
import com.example.vibramobile.data.source.remote.dto.Response
import com.example.vibramobile.data.mapper.toDomain
import com.example.vibramobile.data.mapper.toEntity
import com.example.vibramobile.data.source.local.dao.SongDao
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URLEncoder
import java.text.Normalizer
import kotlinx.serialization.json.Json

class BillRepository(
    private val client: HttpClient,
    private val json: Json,
    private val songDao: SongDao,
    private val context: Context
) : IBillRepository {

    override suspend fun getPaymentHistory(accessToken: String): List<Bill> {
        val response = client.get("profile/payment-history") {
            bearerAuth(accessToken)
        }.bodyAsText()

        val data = json.decodeFromString<Response<List<BillResponseDto>>>(response).data
        cachePaidSongs(data)
        downloadPaidSongs(data)
        return data.map { it.toDomain() }
    }

    private suspend fun cachePaidSongs(bills: List<BillResponseDto>) {
        val songs = bills.asSequence()
            .filter { it.status == "2" }
            .flatMap { bill ->
                val billSongs = mutableListOf<SongResponseDto>()
                bill.song?.let { billSongs.add(it) }
                bill.playlist?.songs?.let { billSongs.addAll(it) }
                billSongs.asSequence()
            }
            .mapNotNull { it.toEntity() }
            .filter { it.id != null }
            .distinctBy { it.id }
            .toList()

        if (songs.isNotEmpty()) {
            songDao.deleteAll()
            songDao.insertAll(songs)
        }
    }

    private suspend fun downloadPaidSongs(bills: List<BillResponseDto>) =
        withContext(Dispatchers.IO) {
            val candidates = bills.asSequence()
                .filter { it.status == "2" }
                .flatMap { bill ->
                    val billSongs = mutableListOf<SongResponseDto>()
                    bill.song?.let { billSongs.add(it) }
                    bill.playlist?.songs?.let { billSongs.addAll(it) }
                    billSongs.asSequence()
                }
                .filter { it.id != null && !it.song_path.isNullOrBlank() }
                .distinctBy { it.id }
                .toList()

            if (candidates.isEmpty()) return@withContext

            val cached = songDao.getAllSongsOnce().associateBy { it.id }
            val songsDir = File(context.filesDir, "offline_songs").apply { mkdirs() }
            val thumbnailsDir = File(context.filesDir, "offline_thumbnails").apply { mkdirs() }
            val avatarsDir = File(context.filesDir, "offline_avatars").apply { mkdirs() }

            for (song in candidates) {
                val songId = song.id ?: continue
                val remotePath = song.song_path ?: continue
                if (!remotePath.startsWith("http")) continue

                val cachedSong = cached[songId]

                val songPath = run {
                    val existingPath = cachedSong?.songPath
                    if (!existingPath.isNullOrBlank() && !existingPath.startsWith("http") && File(
                            existingPath
                        ).exists()
                    ) {
                        existingPath
                    } else {
                        val extension = remotePath.substringAfterLast('.', "mp3")
                        val fileName = "${song.name?.toSafeFileName() ?: "song_$songId"}.$extension"
                        val outputFile = File(songsDir, fileName)
                        if (!outputFile.exists()) {
                            downloadFile(remotePath, outputFile)
                        }
                        if (outputFile.exists()) outputFile.absolutePath else null
                    }
                }

                val thumbnailPath = run {
                    val remoteThumb = song.thumbnail_path
                    val existingPath = cachedSong?.thumbnailPath
                    if (!existingPath.isNullOrBlank() && !existingPath.startsWith("http") && File(
                            existingPath
                        ).exists()
                    ) {
                        existingPath
                    } else if (!remoteThumb.isNullOrBlank() && remoteThumb.startsWith("http")) {
                        val extension = remoteThumb.substringAfterLast('.', "jpg")
                        val fileName =
                            "${song.name?.toSafeFileName() ?: "thumb_$songId"}.$extension"
                        val outputFile = File(thumbnailsDir, fileName)
                        if (!outputFile.exists()) {
                            downloadFile(remoteThumb, outputFile)
                        }
                        if (outputFile.exists()) outputFile.absolutePath else null
                    } else null
                }

                val avatarPath = run {
                    val remoteAvatar = song.author_avatar_path
                    val existingPath = cachedSong?.authorAvatarPath
                    if (!existingPath.isNullOrBlank() && !existingPath.startsWith("http") && File(
                            existingPath
                        ).exists()
                    ) {
                        existingPath
                    } else if (!remoteAvatar.isNullOrBlank() && remoteAvatar.startsWith("http")) {
                        val extension = remoteAvatar.substringAfterLast('.', "jpg")
                        val safeName = song.author_name?.toSafeFileName() ?: "avatar_$songId"
                        val outputFile = File(avatarsDir, "$safeName.$extension")
                        if (!outputFile.exists()) {
                            downloadFile(remoteAvatar, outputFile)
                        }
                        if (outputFile.exists()) outputFile.absolutePath else null
                    } else null
                }

                songDao.updatePaths(
                    id = songId,
                    songPath = songPath,
                    thumbnailPath = thumbnailPath,
                    authorAvatarPath = avatarPath
                )
            }
        }

    private suspend fun downloadFile(url: String, outputFile: File): Boolean {
        return try {
            val channel = client.get(url.encodeUrl()).bodyAsChannel()
            outputFile.outputStream().use { out ->
                channel.toInputStream().use { it.copyTo(out) }
            }
            true
        } catch (e: Exception) {
            outputFile.delete()
            false
        }
    }

    private fun String.encodeUrl(): String {
        val protocolEnd = indexOf("://")
        if (protocolEnd == -1) return this
        val pathStart = indexOf('/', protocolEnd + 3)
        if (pathStart == -1) return this
        val base = substring(0, pathStart)
        val path = substring(pathStart)
        val encodedPath = path.split("/").joinToString("/") { segment ->
            URLEncoder.encode(segment, "UTF-8").replace("+", "%20")
        }
        return base + encodedPath
    }

    private fun String.toSafeFileName(): String {
        val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
        return normalized
            .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
            .replace('đ', 'd').replace('Đ', 'd')
            .lowercase()
            .replace(Regex("[^a-z0-9]+"), "_")
            .trim('_')
    }
}