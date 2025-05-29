package com.myclassroom

import com.myclassroom.config.JwtConfig
import com.myclassroom.data.base.BaseResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurity() {
    install(Authentication) {
        jwt("jwt") {
            realm = "MyClassroom"
            verifier(JwtConfig.getVerifier())
            validate { credential ->
                val userId = credential.payload.getClaim("userId")?.asLong()
                val role = credential.payload.getClaim("role")?.asString()
                if (userId != null && role != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    BaseResponse(false, "Invalid or missing JWT token", null)
                )
            }
        }
    }
}