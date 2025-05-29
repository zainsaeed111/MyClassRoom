package domain

import com.myclassroom.config.JwtConfig
import com.myclassroom.data.authresponse.AuthBaseResponse
import com.myclassroom.data.base.BaseResponse
import requests.UserRegisterRequest
import com.myclassroom.data.models.User
import com.myclassroom.repositories.repositories.UserRepository
import com.myclassroom.data.authresponse.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.mindrot.jbcrypt.BCrypt
import requests.LoginReq
import utills.RegisterError
import java.time.LocalDateTime

class AuthService(private val userRepo: UserRepository) {

    suspend fun register(req: UserRegisterRequest): BaseResponse<AuthBaseResponse> = withContext(Dispatchers.IO) {
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
            val userData = AuthResponse.toResponse(createdUser)
            val registerationResponse = AuthBaseResponse(token, userData)

            BaseResponse(true, "Registration successful", registerationResponse)
        } catch (e: Exception) {
            BaseResponse(false, "Registration failed: ${e.message}")
        }
    }







    suspend fun userLogin(loginReq: LoginReq): BaseResponse<AuthBaseResponse>{
        val user=userRepo.findUserByEmail(loginReq.email)?:return BaseResponse(false,"User not found")

        val isPasswordValid = BCrypt.checkpw(loginReq.password, user.password)
        if (!isPasswordValid){
            return BaseResponse(false,"Password is invalid")
        }

        val token = JwtConfig.generateToken(user)
        return  BaseResponse(true, "Login Successfully", AuthBaseResponse(token, userResponse = AuthResponse.toResponse(user)))
    }

}