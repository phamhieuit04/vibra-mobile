package com.example.vibramobile.core.di

import com.example.vibramobile.data.source.remote.socket.ISocketClient
import com.example.vibramobile.data.source.remote.socket.SocketClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val socketModule = module {
    singleOf(::SocketClient) bind ISocketClient::class
}