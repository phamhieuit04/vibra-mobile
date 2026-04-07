package com.example.vibramobile.repositories

import com.example.vibramobile.contracts.ICategoryRepository
import com.example.vibramobile.models.Category
import com.example.vibramobile.models.Response
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class CategoryRepository(
    private val client: HttpClient,
    private val json: Json
) : ICategoryRepository {
    override suspend fun getCategories(accessToken: String): List<Category> {
        val response = client.get("home/list-category") {
            bearerAuth(accessToken)
        }.bodyAsText()

        return json.decodeFromString<Response<List<Category>>>(response).data
    }
}

