package com.example.vibramobile.domain.contract

import com.example.vibramobile.domain.model.User

interface IAuthRepository {
    suspend fun login(email: String, password: String): User
    suspend fun checkToken(token: String): User?
}
