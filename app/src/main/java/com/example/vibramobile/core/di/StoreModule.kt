package com.example.vibramobile.core.di

import com.example.vibramobile.presentation.state.SessionStore
import org.koin.dsl.module

val storeModule = module {
    single {
        SessionStore(get())
    }
}