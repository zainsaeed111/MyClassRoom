package com.myclassroom.routes

import com.myclassroom.data.base.BaseResponse
import com.myclassroom.domain.ClassroomService
import com.myclassroom.domain.EnrollmentService
import com.myclassroom.repositories.repositories.UserRepository
import com.myclassroom.requests.CreateClassRequest
import com.myclassroom.requests.JoinClassRequest
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

fun Route.classroomRoutes(
    classroomService: ClassroomService,
    userRepository: UserRepository
) {
    route("/classroom") {
        authenticate("jwt") {
            post("/create") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asLong()
                    ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                        BaseResponse<Unit>(false, "Invalid or missing JWT token", null)
                    )

                val user = userRepository.getUserById(userId)
                    ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                        BaseResponse<Unit>(false, "User not found", null)
                    )

                val request = try {
                    call.receive<CreateClassRequest>()
                } catch (e: Exception) {
                    return@post call.respond(
                        HttpStatusCode.BadRequest,
                        BaseResponse<Unit>(false, "Invalid request body: ${e.localizedMessage}", null)
                    )
                }

                val response = classroomService.createClassroom(request, user)

                call.respond(
                    if (response.success) HttpStatusCode.OK else HttpStatusCode.BadRequest,
                    response
                )
            }
        }
    }
}

fun Route.enrollmentRoutes(
    enrollmentService: EnrollmentService,
    userRepository: UserRepository
) {
    route("/enroll") {
        authenticate("jwt") {
            post("/join") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asLong()
                    ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                        BaseResponse<Unit>(false, "Invalid or missing JWT token", null)
                    )

                val request = try {
                    call.receive<JoinClassRequest>()
                } catch (e: Exception) {
                    return@post call.respond(
                        HttpStatusCode.BadRequest,
                        BaseResponse<Unit>(false, "Invalid request body: ${e.localizedMessage}", null)
                    )
                }

                // Validate that the studentId in the request matches the authenticated user
                if (request.studentId != userId) {
                    return@post call.respond(
                        HttpStatusCode.Forbidden,
                        BaseResponse<Unit>(false, "Student ID does not match authenticated user", null)
                    )
                }

                val response = enrollmentService.joinClassByCode(request.classCode, request.studentId)

                val statusCode = when {
                    response.success -> HttpStatusCode.OK
                    response.message.contains("Class not found", ignoreCase = true) -> HttpStatusCode.NotFound
                    response.message.contains("Student not found", ignoreCase = true) -> HttpStatusCode.Unauthorized
                    response.message.contains("already enrolled", ignoreCase = true) -> HttpStatusCode.Conflict
                    else -> HttpStatusCode.BadRequest
                }

                call.respond(statusCode, response)
            }
        }
    }

    get("/students") {
        val logger = LoggerFactory.getLogger("EnrollmentRoutes")
        try {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.payload?.getClaim("userId")?.asLong()
                ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    BaseResponse<Unit>(false, "Invalid or missing JWT token", null)
                )

            // Optional: Restrict to teachers or admins
            val user = userRepository.getUserById(userId)


            val classId = call.parameters["classId"]?.toLongOrNull()
                ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    BaseResponse<Unit>(false, "Missing or invalid classId", null)
                )

            val offset = call.parameters["offset"]?.toLongOrNull() ?: 0
            val limit = call.parameters["limit"]?.toIntOrNull() ?: 20

            logger.info("Fetching enrolled students for classId: $classId, offset: $offset, limit: $limit")

            val response = enrollmentService.getEnrolledStudents(classId, offset, limit)

            val statusCode = when {
                response.success -> HttpStatusCode.OK
                response.message.contains("Class not found", ignoreCase = true) -> HttpStatusCode.NotFound
                else -> HttpStatusCode.InternalServerError
            }

            call.respond(statusCode, response)
        } catch (e: Exception) {
            logger.error("Error in /enroll/students for classId: ${call.parameters["classId"]}", e)
            call.respond(
                HttpStatusCode.InternalServerError,
                BaseResponse<Unit>(false, "Internal server error: ${e.localizedMessage}", null)
            )
        }
    }

}