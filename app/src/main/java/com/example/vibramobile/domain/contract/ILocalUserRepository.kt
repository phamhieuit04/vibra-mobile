package com.example.vibramobile.domain.contract

import com.example.vibramobile.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ILocalUserRepository {
    fun observeUser(): Flow<User?>

    suspend fun getUser(): User?

    suspend fun getAccessToken(): String

    suspend fun upsertUser(user: User)

    suspend fun clearUser()
}
