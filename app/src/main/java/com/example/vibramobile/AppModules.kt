package com.example.vibramobile

import com.example.vibramobile.contracts.IAuthRepository
import com.example.vibramobile.controllers.MediaPlayerController
import com.example.vibramobile.repositories.AuthRepository
import com.example.vibramobile.viewmodels.AuthViewModel
import com.example.vibramobile.viewmodels.ContextMenuViewModel
import com.example.vibramobile.viewmodels.GenreDetailViewModel
import com.example.vibramobile.viewmodels.HomeViewModel
import com.example.vibramobile.viewmodels.MediaPlayerViewModel
import com.example.vibramobile.viewmodels.SearchViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModules = module {
    single<Json> {
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        }
    }

    single {
        MediaPlayerController(androidContext().applicationContext)
    }

    single<HttpClient> {
        val url = "http://100.113.48.99:8000/api/"

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

    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::MediaPlayerViewModel)
    viewModelOf(::ContextMenuViewModel)
    viewModelOf(::GenreDetailViewModel)
    viewModelOf(::SearchViewModel)

    singleOf(::AuthRepository) bind IAuthRepository::class
}
