package routes

import domain.AuthService
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import requests.LoginReq
import requests.UserRegisterRequest

fun Route.authRoutes(authService: AuthService) {
/*
    */
/*route*//*
("/register") {
        post {
            val multipartData = call.receiveMultipart()
            val requestParts = mutableMapOf<String, String>()
            var profilePicInput: PartData.FileItem? = null

            // Collect form fields and profile picture asynchronously
            withContext(Dispatchers.IO) {
                multipartData.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> requestParts[part.name ?: ""] = part.value
                        is PartData.FileItem -> profilePicInput = part
                        is PartData.BinaryChannelItem, is PartData.BinaryItem -> {} // Ignore binary parts
                        else -> part.dispose() // Handle unexpected types
                    }
                    part.dispose() // Release resources immediately
                }
            }

            // Build UserRegisterRequest with basic validation
            val request = UserRegisterRequest(
                fullName = requestParts["fullName"] ?: "",
                userName = requestParts["userName"] ?: "",
                email = requestParts["email"] ?: "",
                password = requestParts["password"] ?: "",
                userRole = UserRole.valueOf(requestParts["userRole"] ?: "STUDENT"),
                phoneNumber = requestParts["phoneNumber"],
                organizationName = requestParts["organizationName"],
                organizationEmail = requestParts["organizationEmail"],
                organizationType = requestParts["organizationType"]
            ).also {
                require(it.fullName.isNotBlank()) { "Full name is required" }
                require(it.email.isNotBlank()) { "Email is required" }
                require(it.password.isNotBlank()) { "Password is required" }
            }

            // Handle profile picture upload with manual stream copy
            val profilePicUrl = profilePicInput?.let { filePart ->
                withContext(Dispatchers.IO) {
                    val fileName = "uploads/profile_${request.email}_${UUID.randomUUID()}.jpg"
                    val file = File(fileName).also { it.parentFile.mkdirs() }
                    val inputStream = BufferedInputStream(filePart.streamProvider().asInput() as InputStream)
                    val outputStream = FileOutputStream(file)
                    try {
                        inputStream.copyTo(outputStream)
                    } finally {
                        inputStream.close()
                        outputStream.close()
                    }
                    "http://localhost:8080/$fileName" // Local URL for development
                }
            }

            val result = authService.register(request.copy(profilePic = profilePicUrl))
            call.respond(result)
        }
    }
*/
    route("/register") {
        post {
            val request = call.receive<UserRegisterRequest>() // Use JSON deserialization
            val result = authService.register(request)
            call.respond(result)
        }
    }
    route("/login") {
        post {
            val request = call.receive<LoginReq>()
            val result = authService.userLogin(request)
            call.respond(result)
        }
    }
}