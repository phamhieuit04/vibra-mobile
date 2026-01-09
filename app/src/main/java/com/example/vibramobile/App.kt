package com.example.vibramobile

import android.app.Application
import com.example.vibramobile.injects.jsonModule
import com.example.vibramobile.injects.mediaPlayerModule
import com.example.vibramobile.injects.networkModule
import com.example.vibramobile.injects.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                networkModule,
                viewModelModule,
                mediaPlayerModule,
                jsonModule
            )
        }
    }
}