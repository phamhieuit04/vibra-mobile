package com.example.vibramobile.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @ColumnInfo(name = "is_dark_mode")
    val isDarkMode: Boolean? = null,

    @ColumnInfo(name = "accent_hex")
    val accentHex: String? = null,
)