package com.example.vibramobile.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.vibramobile.data.source.local.dao.SettingDao
import com.example.vibramobile.data.source.local.dao.UserDao
import com.example.vibramobile.data.source.local.entity.SettingEntity
import com.example.vibramobile.data.source.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        SettingEntity::class
    ],
    version = 10,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun settingDao(): SettingDao
}