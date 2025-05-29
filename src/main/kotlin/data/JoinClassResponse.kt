package com.myclassroom.data


import kotlinx.serialization.Serializable

@Serializable
data class JoinClassResponse(
    val classId: Long,
    val classTitle: String,
    val message: String
)