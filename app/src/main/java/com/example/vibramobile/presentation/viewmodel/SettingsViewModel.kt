package com.example.vibramobile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.domain.contract.ISettingRepository
import com.example.vibramobile.domain.model.Setting
import com.example.vibramobile.presentation.theme.DEFAULT_ACCENT_COLOR_HEX
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingRepository: ISettingRepository
) : ViewModel() {
    private val defaultSetting = Setting(
        id = 1,
        isDarkMode = true,
        accentHex = DEFAULT_ACCENT_COLOR_HEX
    )

    val setting: StateFlow<Setting> = settingRepository.getSetting()
        .map { it ?: defaultSetting }
        .stateIn(viewModelScope, SharingStarted.Eagerly, defaultSetting)

    init {
        viewModelScope.launch {
            if (settingRepository.getSetting().first() == null) {
                settingRepository.insertSetting(defaultSetting)
            }
        }
    }

    fun updateDarkMode(isDarkMode: Boolean) {
        viewModelScope.launch {
            val current = setting.value
            settingRepository.updateSetting(
                current.copy(isDarkMode = isDarkMode, id = 1)
            )
        }
    }

    fun updateAccentColor(accentHex: String) {
        viewModelScope.launch {
            val current = setting.value
            settingRepository.updateSetting(
                current.copy(accentHex = accentHex, id = 1)
            )
        }
    }
}
