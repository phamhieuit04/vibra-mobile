package com.example.vibramobile.data.source.remote.socket

interface ISocketClient {
    fun connect(userId: String)
    fun play(userId: String)
    fun pause(userId: String)
    fun seek(userId: String, positionMs: Long)
    fun observeState(callback: (String) -> Unit)
    fun disconnect()
}