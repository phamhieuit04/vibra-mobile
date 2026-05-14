package com.example.vibramobile.domain.contract

import com.example.vibramobile.domain.model.Setting
import kotlinx.coroutines.flow.Flow

interface ISettingRepository {
    fun getSetting(): Flow<Setting?>

    suspend fun insertSetting(setting: Setting)

    suspend fun updateSetting(setting: Setting)

    suspend fun deleteSetting()
}