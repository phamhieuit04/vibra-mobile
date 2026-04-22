package com.example.vibramobile.domain.contract

import com.example.vibramobile.domain.model.Category

interface ICategoryRepository {
    suspend fun getCategories(accessToken: String): List<Category>
}

