package com.example.vibramobile.data.repository

import com.example.vibramobile.domain.contract.IAuthRepository
import com.example.vibramobile.data.source.remote.dto.Response
import com.example.vibramobile.data.source.remote.dto.UserResponseDto
import com.example.vibramobile.data.source.remote.mapper.toDomain
import com.example.vibramobile.domain.model.User
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.bodyAsText
import io.ktor.http.parameters
import kotlinx.serialization.json.Json

class AuthRepository(
    private val client: HttpClient,
    private val json: Json
) : IAuthRepository {
    override suspend fun login(email: String, password: String): User {
        val response = client.submitForm(
            url = "login",
            formParameters = parameters {
                append(name = "email", value = email)
                append(name = "password", value = password)
            }
        ).bodyAsText()

        return json.decodeFromString<Response<UserResponseDto>>(response).data.toDomain()
    }
}