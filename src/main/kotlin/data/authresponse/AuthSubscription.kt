package com.myclassroom.data.authresponse

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class AuthSubscription(
    val plan: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val startDate: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val endDate: LocalDateTime,
    val isActive: Boolean
) {
    companion object {
        fun default(): AuthSubscription {
            val now = LocalDateTime.now()
            return AuthSubscription(
                plan = "Trial",
                startDate = now,
                endDate = now.plusDays(7),
                isActive = true
            )
        }
    }
}
