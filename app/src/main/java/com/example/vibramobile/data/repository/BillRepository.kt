package com.example.vibramobile.data.repository

import com.example.vibramobile.domain.contract.IBillRepository
import com.example.vibramobile.domain.model.Bill
import com.example.vibramobile.data.source.remote.dto.BillResponseDto
import com.example.vibramobile.data.source.remote.dto.Response
import com.example.vibramobile.data.source.remote.mapper.toDomain
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class BillRepository(
    private val client: HttpClient,
    private val json: Json
) : IBillRepository {
    override suspend fun getPaymentHistory(accessToken: String): List<Bill> {
        val response = client.get("profile/payment-history") {
            bearerAuth(accessToken)
        }.bodyAsText()

        return json.decodeFromString<Response<List<BillResponseDto>>>(response).data.map { it.toDomain() }
    }
}