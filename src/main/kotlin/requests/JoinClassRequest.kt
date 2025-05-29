package com.myclassroom.requests
import kotlinx.serialization.Serializable

@Serializable
data class JoinClassRequest(
    val classCode: String,
    val studentId: Long
)