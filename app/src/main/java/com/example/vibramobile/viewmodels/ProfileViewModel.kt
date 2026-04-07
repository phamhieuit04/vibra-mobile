package com.example.vibramobile.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.contracts.IUserRepository
import com.example.vibramobile.states.UserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: IUserRepository
) : ViewModel() {
    private fun accessToken() = UserState.currentUser.value?.token.orEmpty()

    fun fetchFollowedArtists() {
        viewModelScope.launch {
            try {
                val artists = userRepository.getFollowedArtists(accessToken())
                UserState.setFollowedArtists(artists)
            } catch (exception: Exception) {
                Log.e("MyApp", exception.toString())
            }
        }
    }

    fun fetchMyPlaylists() {
        viewModelScope.launch {
            try {
                val playlists = userRepository.getMyPlaylists(accessToken())
                UserState.setMyPlaylists(playlists)
            } catch (exception: Exception) {
                Log.e("MyApp", exception.toString())
            }
        }
    }
}