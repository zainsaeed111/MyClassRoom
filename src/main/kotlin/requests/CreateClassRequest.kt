package com.myclassroom.requests

import kotlinx.serialization.Serializable


@Serializable
data class CreateClassRequest(
    val classTitle: String,
    val classDescription: String?=null,
    val classSubject:String,
    val classSection: String?=null,
    val classCoverImageUrl: String? = null

)
