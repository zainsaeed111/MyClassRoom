package com.myclassroom.data.models

import com.myclassroom.data.authresponse.AuthSubscription
import com.myclassroom.data.enums.UserRole
import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt
import java.time.LocalDateTime
import java.util.concurrent.Flow

@Serializable
data class User(
    val userId: Long? = null,
    val fullName: String?,
    val userName: String?,
    val email: String?,
    val phoneNumber: String? = null,
    val password: String?=null,
    val userRole: UserRole?,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    val subscription: AuthSubscription =AuthSubscription.default(),
    val organizationName: String? = null,
    val organizationEmail: String? = null,
    val organizationType: String? = null,

) {
    fun withHashedPassword(): User = copy(password = BCrypt.hashpw(password, BCrypt.gensalt()))
}