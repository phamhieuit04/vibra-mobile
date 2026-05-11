package com.example.vibramobile.data.source.remote.socket

import com.example.vibramobile.data.source.remote.config.SOCKET_ENDPOINT
import com.example.vibramobile.data.source.remote.socket.model.SocketRoomState
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONArray
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

    override fun play(userId: Int, songId: Int?) {
        val payload = JSONObject().apply {
            put("userId", userId)
            if (songId != null) {
                put("songId", songId)
            }
        }

        socket.emit("play", payload)
    }

    override fun pause(userId: Int) {
        val payload = JSONObject().apply {
            put("userId", userId)
        }

        socket.emit("pause", payload)
    }

    override fun seek(userId: Int, positionMs: Long) {
        val payload = JSONObject().apply {
            put("userId", userId)
            put("positionMs", positionMs)
        }

        socket.emit("seek", payload)
    }

    override fun queueAdd(userId: Int, songIds: List<Int>) {
        val payload = JSONObject().apply {
            put("userId", userId)
            put("songIds", JSONArray(songIds))
        }

        socket.emit("queue:add", payload)
    }

    override fun observeState(callback: (SocketRoomState) -> Unit) {
        socket.on("state") { args ->
            val data = args[0] as JSONObject

            val player = data.optJSONObject("player") ?: JSONObject()
            val queue = data.optJSONObject("queue") ?: JSONObject()

            val songIds = queue.optJSONArray("songIds")?.toIntList().orEmpty()
            val state = SocketRoomState(
                isPlaying = player.optBoolean("isPlaying", false),
                currentPosition = player.optLong("currentPosition", 0L),
                queueSongIds = songIds,
                currentIndex = queue.optInt("currentIndex", -1),
                timestamp = data.optLong("timestamp", System.currentTimeMillis())
            )

            callback(state)
        }
    }

    override fun disconnect() {
        socket.disconnect()
    }

    private fun JSONArray.toIntList(): List<Int> {
        return (0 until length()).mapNotNull { index ->
            val value = optInt(index, Int.MIN_VALUE)
            if (value == Int.MIN_VALUE) null else value
        }
    }
}