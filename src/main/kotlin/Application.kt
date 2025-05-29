package com.myclassroom
import com.myclassroom.db.DatabaseFactory
import com.myclassroom.domain.ClassroomService
import com.myclassroom.domain.EnrollmentService
import com.myclassroom.repository.implementation.CreateClassRepoImpl
import com.myclassroom.repositories.implementations.EnrollmentRepoImpl
import com.myclassroom.routes.classroomRoutes
import com.myclassroom.routes.enrollmentRoutes
import domain.AuthService
import io.ktor.server.application.*
import io.ktor.server.routing.routing
import repositories.implementations.UserRepositoryImpl
import routes.authRoutes

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureSerialization()

    DatabaseFactory.init()

    val userRepo = UserRepositoryImpl()
    val classRoomRepo = CreateClassRepoImpl()
    val enrollmentRepo = EnrollmentRepoImpl()

    val authService = AuthService(userRepo)
    val classRoomService = ClassroomService(classRoomRepo)
    val enrollmentService = EnrollmentService(enrollmentRepo)

    // Routes
    routing {
        authRoutes(authService)
        classroomRoutes(classRoomService, userRepo)
        enrollmentRoutes(enrollmentService, userRepo)
    }
}
