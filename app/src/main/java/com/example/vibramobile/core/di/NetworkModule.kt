package com.example.vibramobile.core.di

import com.example.vibramobile.data.source.remote.config.API_BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single<HttpClient> {
        val url = API_BASE_URL

        HttpClient() {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            defaultRequest {
                url(url)
                headers.append(HttpHeaders.ContentType, "application/json; charset=UTF-8")
            }
        }
    }
}