package com.example.vibramobile.data.repository

import com.example.vibramobile.domain.contract.ISearchResultRepository
import com.example.vibramobile.data.source.remote.dto.Response
import com.example.vibramobile.data.source.remote.dto.SearchResultResponseDto
import com.example.vibramobile.data.source.remote.mapper.toDomain
import com.example.vibramobile.domain.model.SearchResult
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class SearchResultRepository(
    private val client: HttpClient,
    private val json: Json
) : ISearchResultRepository {
    override suspend fun search(keyword: String, accessToken: String): SearchResult {
        val response = client.get("home/search?search-key=$keyword") {
            bearerAuth(accessToken)
        }.bodyAsText()

        return json.decodeFromString<Response<SearchResultResponseDto>>(response).data.toDomain()
    }
}

