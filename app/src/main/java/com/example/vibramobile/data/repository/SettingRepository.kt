package com.example.vibramobile.data.repository

import com.example.vibramobile.data.mapper.toDomain
import com.example.vibramobile.data.mapper.toEntity
import com.example.vibramobile.data.source.local.dao.SettingDao
import com.example.vibramobile.domain.contract.ISettingRepository
import com.example.vibramobile.domain.model.Setting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SettingRepository(
    private val settingDao: SettingDao
) : ISettingRepository {

    override fun getSetting(): Flow<Setting?> {
        return settingDao.getSetting().map { it?.toDomain() }
    }

    override suspend fun insertSetting(setting: Setting) {
        withContext(Dispatchers.IO) {
            settingDao.insert(setting.toEntity())
        }
    }

    override suspend fun updateSetting(setting: Setting) {
        withContext(Dispatchers.IO) {
            settingDao.update(setting.toEntity())
        }
    }

    override suspend fun deleteSetting() {
        withContext(Dispatchers.IO) {
            settingDao.clear()
        }
    }
}