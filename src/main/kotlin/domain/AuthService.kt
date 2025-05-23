package domain

import com.myclassroom.config.JwtConfig
import com.myclassroom.data.AuthResponse
import com.myclassroom.data.BaseResponse
import com.myclassroom.data.UserRegisterRequest
import com.myclassroom.data.User
import com.myclassroom.repositories.UserRepository
import data.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import utills.RegisterError
import java.time.LocalDateTime

class AuthService(private val userRepo: UserRepository) {

    suspend fun register(req: UserRegisterRequest): BaseResponse<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            if (req.email.isBlank()) {
                return@withContext BaseResponse(false, RegisterError.EmailEmpty.message)
            }

            if (userRepo.findUserByEmail(req.email) != null) {
                return@withContext BaseResponse(false, RegisterError.EmailExists.message)
            }

            val user = User(
                userId = null,
                fullName = req.fullName,
                userName = req.userName,
                email = req.email,
                phoneNumber = req.phoneNumber,
                password = req.password,
                userRole = req.userRole,
                createdAt = LocalDateTime.now()
            ).withHashedPassword()

            val isCreated = userRepo.createUser(user)
            if (!isCreated) {
                return@withContext BaseResponse(false, RegisterError.DatabaseError.message)
            }

            // Fetch the created user to get the generated ID
            val createdUser = userRepo.findUserByEmail(user.email) ?:
            return@withContext BaseResponse(false, RegisterError.DatabaseError.message)

            val token = JwtConfig.generateToken(createdUser)
            val userData = UserData.toResponse(createdUser)
            val authToken = AuthResponse(token, userData)

            BaseResponse(true, "Registration successful", authToken)
        } catch (e: Exception) {
            BaseResponse(false, "Registration failed: ${e.message}")
        }
    }
}