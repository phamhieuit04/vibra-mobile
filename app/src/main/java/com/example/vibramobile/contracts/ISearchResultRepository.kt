package com.example.vibramobile.contracts

import com.example.vibramobile.models.SearchResult

interface ISearchResultRepository {
    suspend fun search(keyword: String, accessToken: String): SearchResult
}

