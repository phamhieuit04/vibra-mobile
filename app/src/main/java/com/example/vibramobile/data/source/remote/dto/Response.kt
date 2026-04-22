package com.example.vibramobile.data.source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(
    var code: Int? = null,
    var data: T,
    var message: String? = null
)
