package com.example.vibramobile.injects

import kotlinx.serialization.json.Json
import org.koin.dsl.module

val jsonModule = module {
    single<Json> {
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
        }
    }
}