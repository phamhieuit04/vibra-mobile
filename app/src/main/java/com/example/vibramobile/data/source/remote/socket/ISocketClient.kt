package com.example.vibramobile.data.source.remote.socket

import com.example.vibramobile.data.source.remote.socket.model.SocketRoomState

interface ISocketClient {
    fun connect(userId: Int)
    fun play(userId: Int, songId: Int? = null)
    fun pause(userId: Int)
    fun seek(userId: Int, positionMs: Long)
    fun queueAdd(userId: Int, songIds: List<Int>)
    fun shuffle(userId: Int, isShuffleEnabled: Boolean)
    fun repeat(userId: Int, repeatMode: String)
    fun next(userId: Int)
    fun previous(userId: Int)
    fun trackEnded(userId: Int)
    fun observeState(callback: (SocketRoomState) -> Unit)
    fun disconnect()
}