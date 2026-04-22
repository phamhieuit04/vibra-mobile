package com.example.vibramobile.data.mapper

import com.example.vibramobile.data.source.remote.dto.UserResponseDto
import com.example.vibramobile.domain.model.User

fun UserResponseDto.toDomain(): User {
    return User(
        id = id,
        name = name,
        description = description,
        email = email,
        avatar = avatar,
        followers = followers,
        token = token,
        avatarPath = avatar_path
    )
}