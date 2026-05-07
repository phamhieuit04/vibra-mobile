package com.example.vibramobile.data.source.remote.socket

import io.socket.client.Socket

class SocketClient : ISocketClient {
    private lateinit var socket: Socket

    override fun connect(userId: String) {
        TODO("Not yet implemented")
    }

    override fun play(userId: String) {
        TODO("Not yet implemented")
    }

    override fun pause(userId: String) {
        TODO("Not yet implemented")
    }

    override fun seek(userId: String, positionMs: Long) {
        TODO("Not yet implemented")
    }

    override fun observeState(callback: (String) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun disconnect() {
        TODO("Not yet implemented")
    }
}