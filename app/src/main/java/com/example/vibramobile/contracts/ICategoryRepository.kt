package com.example.vibramobile.contracts

import com.example.vibramobile.models.Category

interface ICategoryRepository {
    suspend fun getCategories(accessToken: String): List<Category>
}

