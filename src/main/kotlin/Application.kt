package com.myclassroom

import com.myclassroom.config.JwtConfig
import com.myclassroom.db.DatabaseFactory
import com.myclassroom.domain.ClassroomService
import com.myclassroom.repository.implementation.CreateClassRepoImpl
import com.myclassroom.routes.classroomRoutes
import domain.AuthService
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
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
    val authService = AuthService(userRepo)
 val classRoomRepo= CreateClassRepoImpl()
    val classRoomService = ClassroomService(classRoomRepo)
    routing {
        authRoutes(authService)
        classroomRoutes(classRoomService,userRepo)
    }
}
