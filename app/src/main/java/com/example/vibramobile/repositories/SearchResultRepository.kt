package com.example.vibramobile.repositories

import com.example.vibramobile.contracts.ISearchResultRepository
import com.example.vibramobile.models.Response
import com.example.vibramobile.models.SearchResult
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

        return json.decodeFromString<Response<SearchResult>>(response).data
    }
}

