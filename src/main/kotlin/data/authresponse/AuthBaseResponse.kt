package com.myclassroom.data.authresponse

import kotlinx.serialization.Serializable

@Serializable
data class AuthBaseResponse(
    val token: String,
    val userResponse: AuthResponse
)