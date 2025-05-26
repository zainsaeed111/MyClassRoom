package com.myclassroom.routes

import com.myclassroom.data.base.BaseResponse
import com.myclassroom.data.models.Classroom
import com.myclassroom.domain.ClassroomService
import com.myclassroom.requests.CreateClassRequest
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.classroomRoutes(classroomService: ClassroomService) {

    route("/classroom") {

        post("/create") {
            val classroom = call.receive<CreateClassRequest>()
            val response = classroomService.createClassroom(classroom)
            call.respond(response)
        }

        get("/all") {
            val response = classroomService.getAllClassrooms()
            call.respond(response)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(BaseResponse<Unit>(false, "Invalid classroom ID"))
                return@get
            }

            val response = classroomService.getClassroomById(id)
            call.respond(response)
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(BaseResponse<Unit>(false, "Invalid classroom ID"))
                return@delete
            }

            val response = classroomService.deleteClassroom(id)
            call.respond(response)
        }
    }
}
