package com.example.vibramobile.contracts

import com.example.vibramobile.models.User

interface IAuthRepository {
	suspend fun login(email: String, password: String): User
}
