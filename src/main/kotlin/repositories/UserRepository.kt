package com.myclassroom.repositories

import com.myclassroom.data.User

interface UserRepository {
    suspend fun createUser(user: User): Boolean
    suspend fun findUserByEmail(email: String): User?
}