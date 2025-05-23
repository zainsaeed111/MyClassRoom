package routes

import com.myclassroom.data.UserRegisterRequest
import domain.AuthService
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.authRoutes(authService: AuthService) {
    route("/register") {
        post {
            val request = call.receive<UserRegisterRequest>()
            val result = authService.register(request)
            call.respond(result)
        }
    }
}
