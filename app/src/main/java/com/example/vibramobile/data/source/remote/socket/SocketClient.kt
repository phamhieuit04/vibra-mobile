package com.example.vibramobile.data.source.remote.socket

import com.example.vibramobile.data.source.remote.config.SOCKET_ENDPOINT
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class SocketClient : ISocketClient {
    private lateinit var socket: Socket

    override fun connect(userId: Int) {
        val uri = SOCKET_ENDPOINT

        val options = IO.Options().apply {
            reconnection = true
            forceNew = true
            transports = arrayOf("websocket")
        }

        val payload = JSONObject().apply {
            put("userId", userId)
        }

        socket = IO.socket(uri, options)
        socket.connect()

        socket.on(Socket.EVENT_CONNECT) {
            socket.emit("join", payload)
        }
    }

    override fun play(userId: Int) {
        TODO("Not yet implemented")
    }

    override fun pause(userId: Int) {
        TODO("Not yet implemented")
    }

    override fun seek(userId: Int, positionMs: Long) {
        TODO("Not yet implemented")
    }

    override fun observeState(callback: (String) -> Unit) {
        socket.on("state") { args ->
            callback(args[0].toString())
        }
    }

    override fun disconnect() {
        socket.disconnect()
    }
}