package com.example.vibramobile.data.source.remote.socket

interface ISocketClient {
    fun connect(userId: Int)
    fun play(userId: Int)
    fun pause(userId: Int)
    fun seek(userId: Int, positionMs: Long)
    fun observeState(callback: (String) -> Unit)
    fun disconnect()
}