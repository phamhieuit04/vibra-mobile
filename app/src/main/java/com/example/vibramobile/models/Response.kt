package com.example.vibramobile.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Response<T>(
    var code: Int? = null,
    var data: T,
    var message: String? = null
)
