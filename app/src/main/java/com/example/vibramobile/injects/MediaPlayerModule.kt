package com.example.vibramobile.injects

import com.example.vibramobile.controllers.MediaPlayerController
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mediaPlayerModule = module {
    single {
        MediaPlayerController(androidContext().applicationContext)
    }
}