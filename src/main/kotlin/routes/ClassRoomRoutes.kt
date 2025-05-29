package com.myclassroom.routes
import com.myclassroom.data.base.BaseResponse
import com.myclassroom.domain.ClassroomService
import com.myclassroom.repositories.repositories.UserRepository
import com.myclassroom.requests.CreateClassRequest
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.classroomRoutes(classroomService: ClassroomService, userRepository: UserRepository) {
    route("/classroom") {
        authenticate("jwt") {
            post("/create") {
                // Extract user ID from JWT
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asLong()
                    ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                        BaseResponse(false, "Invalid or missing JWT token", null)
                    )

                // Fetch user from database
                val user = userRepository.getUserById(userId)
                    ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                        BaseResponse(false, "User not found", null)
                    )

                // Receive request body
                val request = try {
                    call.receive<CreateClassRequest>()
                } catch (e: Exception) {
                    return@post call.respond(
                        HttpStatusCode.BadRequest,
                        BaseResponse(false, "Invalid request body: ${e.localizedMessage}", null)
                    )
                }

                // Call service
                val response = classroomService.createClassroom(request, user)
                // Set HTTP status based on response.success
                call.respond(
                    if (response.success) HttpStatusCode.OK else HttpStatusCode.BadRequest,
                    response
                )
            }
        }
    }
}