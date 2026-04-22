package com.example.vibramobile.domain.contract

import com.example.vibramobile.domain.model.SearchResult

interface ISearchResultRepository {
    suspend fun search(keyword: String, accessToken: String): SearchResult
}

