package com.myclassroom.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.myclassroom.data.models.User
import java.util.*

object JwtConfig {
    private const val DEFAULT_SECRET = "default-dev-secret-please-change-in-prod"
    private const val DEFAULT_ISSUER = "MyClassroomDev"
    private const val DEFAULT_AUDIENCE = "MyClassroomAudience"
    private const val DEFAULT_EXPIRY_MS = 86_400_000L // 24 hours

    private val secret: String = System.getenv("JWT_SECRET") ?: DEFAULT_SECRET
    private val issuer: String = System.getenv("JWT_ISSUER") ?: DEFAULT_ISSUER
    private val audience: String = System.getenv("JWT_AUDIENCE") ?: DEFAULT_AUDIENCE
    private val expiryMs: Long = System.getenv("JWT_EXPIRES_MS")?.toLongOrNull() ?: DEFAULT_EXPIRY_MS

    private val algorithm: Algorithm = Algorithm.HMAC256(secret)

    fun generateToken(user: User): String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("userId", user.userId)
            .withClaim("email", user.email)
            .withClaim("role", user.userRole.name)
            .withExpiresAt(Date(System.currentTimeMillis() + expiryMs))
            .sign(algorithm)
    }

    fun getVerifier() = JWT.require(algorithm)
        .withAudience(audience)
        .withIssuer(issuer)
        .build()
}