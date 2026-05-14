package com.example.vibramobile.data.mapper

import com.example.vibramobile.data.source.local.entity.SettingEntity
import com.example.vibramobile.domain.model.Setting

fun SettingEntity.toDomain(): Setting {
    return Setting(
        id = id,
        isDarkMode = isDarkMode,
        accentHex = accentHex
    )
}

fun Setting.toEntity(): SettingEntity {
    return SettingEntity(
        id = id,
        isDarkMode = isDarkMode,
        accentHex = accentHex
    )
}