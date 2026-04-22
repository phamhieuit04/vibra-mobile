package com.example.vibramobile.data.mapper

import com.example.vibramobile.data.source.remote.dto.CategoryResponseDto
import com.example.vibramobile.domain.model.Category

fun CategoryResponseDto.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        description = description,
        thumbnailPath = thumbnail_path
    )
}