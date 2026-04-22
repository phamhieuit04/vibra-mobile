package com.example.vibramobile.data.repository

import com.example.vibramobile.domain.contract.ICategoryRepository
import com.example.vibramobile.domain.model.Category
import com.example.vibramobile.data.source.remote.dto.CategoryResponseDto
import com.example.vibramobile.data.source.remote.dto.Response
import com.example.vibramobile.data.source.remote.mapper.toDomain
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

        return json.decodeFromString<Response<List<CategoryResponseDto>>>(response).data.map { it.toDomain() }
    }
}

