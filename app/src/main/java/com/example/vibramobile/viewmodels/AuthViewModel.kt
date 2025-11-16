package com.example.vibramobile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.helpers.Navigator
import com.example.vibramobile.ui.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {
    fun login() {
        viewModelScope.launch {
            Navigator.navigate(destination = Destination.Login)
        }
    }

    fun signup() {
        viewModelScope.launch {
            Navigator.navigate(destination = Destination.SignUp)
        }
    }
}