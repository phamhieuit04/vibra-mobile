package com.example.vibramobile.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.vibramobile.models.Category

object CategoryState {
    var isCategoriesLoading by mutableStateOf(false)

    var categories = mutableStateListOf<Category>()
}