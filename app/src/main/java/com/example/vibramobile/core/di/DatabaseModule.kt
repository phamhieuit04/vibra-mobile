package com.example.vibramobile.core.di

import androidx.room.Room
import com.example.vibramobile.data.source.local.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "vibra.db"
        ).fallbackToDestructiveMigration(true)
            .build()
    }

    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().settingDao() }
    single { get<AppDatabase>().songDao() }
}
