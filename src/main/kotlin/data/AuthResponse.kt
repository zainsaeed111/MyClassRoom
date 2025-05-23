package com.myclassroom.data

import data.UserData
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String,
    val userResponse: UserData
)
