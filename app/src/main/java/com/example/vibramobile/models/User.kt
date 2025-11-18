package com.example.vibramobile.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    var id: Int? = null,
    var name: String? = null,
    var description: String? = null,
    var email: String? = null,
    var avatar: String? = null,
    var followers: Int? = null,
    var token: String? = null,
    var avatar_path: String? = null
)
