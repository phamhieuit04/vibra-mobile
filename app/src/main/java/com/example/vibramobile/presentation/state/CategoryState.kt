package com.example.vibramobile.presentation.state

import com.example.vibramobile.domain.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object CategoryState {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    fun setCategories(newCategories: List<Category>) {
        _categories.value = newCategories
    }
}