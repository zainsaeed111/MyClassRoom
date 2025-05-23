package data

import com.myclassroom.data.UserRole
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class UserData(
    val userId: Long?,
    val fullName: String,
    val userName: String,
    val email: String,
    val phoneNumber: String?,
    val userRole: UserRole,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime
) {
    companion object {
        fun toResponse(user: com.myclassroom.data.User): UserData = UserData(
            userId = user.userId,
            fullName = user.fullName,
            userName = user.userName,
            email = user.email,
            phoneNumber = user.phoneNumber,
            userRole = user.userRole,
            createdAt = user.createdAt?: LocalDateTime.now(),
        )
    }
}
