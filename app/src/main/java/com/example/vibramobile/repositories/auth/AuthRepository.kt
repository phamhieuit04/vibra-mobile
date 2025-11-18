package com.example.vibramobile.repositories.auth

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.HttpResponse
import io.ktor.http.parameters
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val client: HttpClient
) {
    suspend fun login(email: String, password: String): HttpResponse? {
        try {
            return client.submitForm(
                url = "login",
                formParameters = parameters {
                    append(name = "email", value = email)
                    append(name = "password", value = password)
                })
        } catch (e: Exception) {
            Log.i("MyApp", e.toString())

            return null
        }
    }
}