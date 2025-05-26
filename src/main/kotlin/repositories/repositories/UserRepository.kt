package com.myclassroom.repositories.repositories

import com.myclassroom.data.models.User

interface UserRepository {
    suspend fun createUser(user: User): Boolean
    suspend fun findUserByEmail(email: String): User?
}