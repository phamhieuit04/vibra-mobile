package com.example.vibramobile.data.mapper

import com.example.vibramobile.data.source.remote.dto.BillResponseDto
import com.example.vibramobile.domain.model.Bill

fun BillResponseDto.toDomain(): Bill {
    return Bill(
        id = id,
        userId = user_id,
        orderCode = order_code,
        playlistId = playlist_id,
        status = status,
        createdAt = created_at,
        song = song?.toDomain(),
        playlist = playlist?.toDomain()
    )
}