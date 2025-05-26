package requests

import kotlinx.serialization.Serializable

@Serializable
data class LoginReq(
    val email: String,
    val password: String
)
