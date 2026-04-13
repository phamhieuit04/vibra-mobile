package com.example.vibramobile.repositories

import android.util.Log
import com.example.vibramobile.contracts.IBillRepository
import com.example.vibramobile.models.Bill
import com.example.vibramobile.models.Response
import com.example.vibramobile.models.User
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

        return json.decodeFromString<Response<List<Bill>>>(response).data
    }
}