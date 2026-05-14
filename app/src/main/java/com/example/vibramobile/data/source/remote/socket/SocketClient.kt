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

    override fun pause(userId: Int, positionMs: Long) {
        val payload = JSONObject().apply {
            put("userId", userId)
            put("positionMs", positionMs)
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

    override fun shuffle(userId: Int, isShuffleEnabled: Boolean) {
        val payload = JSONObject().apply {
            put("userId", userId)
            put("isShuffleEnabled", isShuffleEnabled)
        }

        socket.emit("shuffle", payload)
    }

    override fun repeat(userId: Int, repeatMode: String) {
        val payload = JSONObject().apply {
            put("userId", userId)
            put("repeatMode", repeatMode)
        }

        socket.emit("repeat", payload)
    }

    override fun next(userId: Int) {
        val payload = JSONObject().apply {
            put("userId", userId)
        }

        socket.emit("next", payload)
    }

    override fun previous(userId: Int) {
        val payload = JSONObject().apply {
            put("userId", userId)
        }

        socket.emit("previous", payload)
    }

    override fun trackEnded(userId: Int) {
        val payload = JSONObject().apply {
            put("userId", userId)
        }

        socket.emit("trackEnded", payload)
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
                startedAt = if (player.has("startedAt") && !player.isNull("startedAt")) {
                    player.optLong("startedAt")
                } else {
                    null
                },
                isShuffleEnabled = player.optBoolean("isShuffleEnabled", false),
                repeatMode = player.optString("repeatMode", "OFF")
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