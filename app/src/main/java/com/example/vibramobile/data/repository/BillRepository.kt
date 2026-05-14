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
            val targetDir = File(context.filesDir, "offline_songs").apply { mkdirs() }

            for (song in candidates) {
                val songId = song.id ?: continue
                val remotePath = song.song_path ?: continue

                if (!remotePath.startsWith("http")) continue

                val cachedPath = cached[songId]?.songPath
                if (!cachedPath.isNullOrBlank() && !cachedPath.startsWith("http")) {
                    if (File(cachedPath).exists()) continue
                }

                val extension = remotePath.substringAfterLast('.', "mp3")
                val songTitle = song.name?.toSafeFileName() ?: "song_$songId"
                val outputFile = File(targetDir, "$songTitle.$extension")

                if (outputFile.exists()) {
                    songDao.updateSongPath(songId, outputFile.absolutePath)
                    continue
                }

                val success = try {
                    val channel = client.get(remotePath.encodeUrl()).bodyAsChannel()
                    outputFile.outputStream().use { out ->
                        channel.toInputStream().use { it.copyTo(out) }
                    }
                    true
                } catch (e: Exception) {
                    outputFile.delete()
                    false
                }

                if (success) {
                    songDao.updateSongPath(songId, outputFile.absolutePath)
                }
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