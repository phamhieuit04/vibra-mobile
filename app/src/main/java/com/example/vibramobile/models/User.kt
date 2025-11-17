package com.example.vibramobile.models

data class User(
    var id: Int,
    var name: String,
    var description: String,
    var email: String,
    var avatar: String,
    var followers: Int
)
