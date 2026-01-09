package com.example.vibramobile.injects

import com.example.vibramobile.viewmodels.AuthViewModel
import com.example.vibramobile.viewmodels.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::HomeViewModel)
}