package com.myclassroom.data.base

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)