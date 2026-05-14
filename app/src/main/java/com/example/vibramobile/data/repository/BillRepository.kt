package com.example.vibramobile.data.repository

import com.example.vibramobile.domain.contract.IBillRepository
import com.example.vibramobile.domain.model.Bill
import com.example.vibramobile.data.source.remote.dto.BillResponseDto
import com.example.vibramobile.data.source.remote.dto.Response
import com.example.vibramobile.data.mapper.toDomain
import com.example.vibramobile.data.mapper.toEntity
import com.example.vibramobile.data.source.local.dao.SongDao
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class BillRepository(
    private val client: HttpClient,
    private val json: Json,
    private val songDao: SongDao
) : IBillRepository {
    override suspend fun getPaymentHistory(accessToken: String): List<Bill> {
        val response = client.get("profile/payment-history") {
            bearerAuth(accessToken)
        }.bodyAsText()

        val data = json.decodeFromString<Response<List<BillResponseDto>>>(response).data
        cachePaidSongs(data)
        return data.map { it.toDomain() }
    }

    private suspend fun cachePaidSongs(bills: List<BillResponseDto>) {
        val songs = bills.asSequence()
            .filter { it.status == "2" }
            .flatMap { bill ->
                val billSongs = mutableListOf<com.example.vibramobile.data.source.remote.dto.SongResponseDto>()
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
}