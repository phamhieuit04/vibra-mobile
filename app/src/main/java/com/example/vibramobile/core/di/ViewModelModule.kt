package com.example.vibramobile.core.di

import com.example.vibramobile.presentation.viewmodel.AlbumDetailViewModel
import com.example.vibramobile.presentation.viewmodel.ArtistDetailViewModel
import com.example.vibramobile.presentation.viewmodel.AuthViewModel
import com.example.vibramobile.presentation.viewmodel.ContextMenuViewModel
import com.example.vibramobile.presentation.viewmodel.GenreDetailViewModel
import com.example.vibramobile.presentation.viewmodel.HomeViewModel
import com.example.vibramobile.presentation.viewmodel.LibraryViewModel
import com.example.vibramobile.presentation.viewmodel.MediaPlayerViewModel
import com.example.vibramobile.presentation.viewmodel.ProfileViewModel
import com.example.vibramobile.presentation.viewmodel.SearchViewModel
import com.example.vibramobile.presentation.viewmodel.SettingsViewModel
import com.example.vibramobile.presentation.viewmodel.SplashViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::MediaPlayerViewModel)
    viewModelOf(::ContextMenuViewModel)
    viewModelOf(::GenreDetailViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::LibraryViewModel)
    viewModelOf(::ArtistDetailViewModel)
    viewModelOf(::AlbumDetailViewModel)
    viewModelOf(::SplashViewModel)
    viewModelOf(::SettingsViewModel)
}