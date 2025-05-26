package com.myclassroom.data.models

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
@Serializable
data class Classroom(
    val classId: Long = 0L,
    val classTitle: String,
    val classDescription: String?,
    val classSubject: String,
    val classSection: String?,
    val classImageUrl: String?,
    val classCode: String,
    val createdBy: Long,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime? = LocalDateTime.now())