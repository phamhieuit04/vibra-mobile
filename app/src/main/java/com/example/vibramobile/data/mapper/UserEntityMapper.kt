package com.example.vibramobile.data.mapper

import com.example.vibramobile.data.source.local.entity.UserEntity
import com.example.vibramobile.domain.model.User

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        name = name,
        description = description,
        email = email,
        avatar = avatar,
        followers = followers,
        token = token,
        avatarPath = avatarPath
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        name = name,
        description = description,
        email = email,
        avatarPath = avatarPath,
        avatar = avatar,
        followers = followers,
        token = token
    )
}
