package com.example.vibramobile.repositories

import com.example.vibramobile.contracts.IAuthRepository
import com.example.vibramobile.models.Response
import com.example.vibramobile.models.User
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

        return json.decodeFromString<Response<User>>(response).data
    }
}