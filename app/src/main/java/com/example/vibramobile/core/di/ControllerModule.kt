package com.example.vibramobile.core.di

import com.example.vibramobile.presentation.controller.MediaPlayerController
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val controllerModule = module {
    single {
        MediaPlayerController(androidContext().applicationContext)
    }
}