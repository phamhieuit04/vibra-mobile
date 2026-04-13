package com.example.vibramobile.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.contracts.IBillRepository
import com.example.vibramobile.contracts.IPlaylistRepository
import com.example.vibramobile.contracts.IUserRepository
import com.example.vibramobile.states.UserState
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: IUserRepository,
    private val playlistRepository: IPlaylistRepository,
    private val billRepository: IBillRepository
) : ViewModel() {
    private fun accessToken() = UserState.currentUser.value?.token.orEmpty()

    fun refresh() {
        fetchProfile()
        fetchFollowedArtists()
        fetchMyPlaylists()
        fetchMyAlbums()
        fetchPaymentHistory()
    }

    fun fetchProfile() {
        viewModelScope.launch {
            try {
                val profile = userRepository.getProfile(accessToken())
                UserState.setCurrentUser(profile)
            } catch (exception: Exception) {
                Log.e("MyApp", exception.toString())
            }
        }
    }

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
                val playlists = playlistRepository.getMyPlaylists(accessToken())
                UserState.setMyPlaylists(playlists)
            } catch (exception: Exception) {
                Log.e("MyApp", exception.toString())
            }
        }
    }

    fun fetchMyAlbums() {
        viewModelScope.launch {
            try {
                val albums = playlistRepository.getMyAlbums(accessToken())
                UserState.setMyAlbums(albums)
            } catch (exception: Exception) {
                Log.e("MyApp", exception.toString())
            }
        }
    }

    fun fetchPaymentHistory() {
        viewModelScope.launch {
            try {
                val bills = billRepository.getPaymentHistory(accessToken())
                UserState.setPaymentHistory(bills)
            } catch (exception: Exception) {
                Log.e("MyApp", exception.toString())
            }
        }
    }
}