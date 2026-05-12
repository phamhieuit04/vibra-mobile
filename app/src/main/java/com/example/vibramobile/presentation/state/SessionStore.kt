package com.example.vibramobile.presentation.state

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException

private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "session_store")

class SessionStore(
    private val context: Context
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _accessToken = MutableStateFlow("")
    val accessToken: StateFlow<String> = _accessToken.asStateFlow()

    private val _isLoaded = MutableStateFlow(false)
    val isLoaded: StateFlow<Boolean> = _isLoaded.asStateFlow()

    private val accessTokenKey = stringPreferencesKey("access_token")

    init {
        scope.launch {
            context.sessionDataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map { preferences -> preferences[accessTokenKey].orEmpty() }
                .collect { token ->
                    _accessToken.value = token
                    _isLoaded.value = true
                }
        }
    }

    suspend fun awaitAccessToken(): String {
        isLoaded.first { it }
        return _accessToken.value
    }

    suspend fun saveAccessToken(token: String) {
        _accessToken.value = token
        context.sessionDataStore.edit { preferences ->
            if (token.isBlank()) {
                preferences.remove(accessTokenKey)
            } else {
                preferences[accessTokenKey] = token
            }
        }
    }

    suspend fun clear() {
        saveAccessToken("")
    }

    fun currentAccessToken(): String = _accessToken.value
}
