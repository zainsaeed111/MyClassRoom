package com.myclassroom.data.authresponse

import com.myclassroom.data.models.User
import com.myclassroom.data.enums.UserRole
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class AuthResponse(
    val userId: Long?,
    val fullName: String?,
    val userName: String?,
    val email: String?,
    val phoneNumber: String?,
    val userRole: UserRole?,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime
) {
    companion object {
        fun toResponse(user: User): AuthResponse = AuthResponse(
            userId = user?.userId,
            fullName = user.fullName,
            userName = user?.userName,
            email = user.email,
            phoneNumber = user.phoneNumber,
            userRole = user.userRole,
            createdAt = user.createdAt?: LocalDateTime.now(),
        )
    }
}