package com.example.vibramobile.core.di

import com.example.vibramobile.domain.contract.IAuthRepository
import com.example.vibramobile.domain.contract.IBillRepository
import com.example.vibramobile.domain.contract.ICategoryRepository
import com.example.vibramobile.domain.contract.IPlaylistRepository
import com.example.vibramobile.domain.contract.ISearchResultRepository
import com.example.vibramobile.domain.contract.ISongRepository
import com.example.vibramobile.domain.contract.IUserRepository
import com.example.vibramobile.domain.contract.ILocalUserRepository
import com.example.vibramobile.domain.contract.ISettingRepository
import com.example.vibramobile.data.repository.AuthRepository
import com.example.vibramobile.data.repository.BillRepository
import com.example.vibramobile.data.repository.CategoryRepository
import com.example.vibramobile.data.repository.LocalUserRepository
import com.example.vibramobile.data.repository.PlaylistRepository
import com.example.vibramobile.data.repository.SearchResultRepository
import com.example.vibramobile.data.repository.SettingRepository
import com.example.vibramobile.data.repository.SongRepository
import com.example.vibramobile.data.repository.UserRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::AuthRepository) bind IAuthRepository::class
    singleOf(::CategoryRepository) bind ICategoryRepository::class
    singleOf(::PlaylistRepository) bind IPlaylistRepository::class
    singleOf(::SongRepository) bind ISongRepository::class
    singleOf(::UserRepository) bind IUserRepository::class
    singleOf(::SearchResultRepository) bind ISearchResultRepository::class
    singleOf(::BillRepository) bind IBillRepository::class
    singleOf(::SettingRepository) bind ISettingRepository::class
    singleOf(::LocalUserRepository) bind ILocalUserRepository::class
}