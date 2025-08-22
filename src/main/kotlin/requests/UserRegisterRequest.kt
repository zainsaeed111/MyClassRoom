package requests

import com.myclassroom.data.enums.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class UserRegisterRequest(
    val fullName: String,
    val userName: String,
    val email: String,
    val phoneNumber: String? = null,
    val password: String,
    val userRole: UserRole,
    val organizationName: String? = null,
    val organizationEmail: String? = null,
    val organizationType: String? = null,
    val profilePic: String? = null
) {
    init {
        require(fullName.isNotBlank()) { "Full name cannot be blank" }
        require(userName.isNotBlank() && userName.length >= 3) { "Username must be at least 3 characters" }
        require(email.isNotBlank() && email.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$"))) { "Invalid email format" }
        require(password.length >= 8) { "Password must be at least 8 characters" }
        if (organizationEmail != null && organizationEmail.isNotBlank()) {
            require(organizationEmail.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$"))) { "Invalid organization email format" }
        }
    }
}