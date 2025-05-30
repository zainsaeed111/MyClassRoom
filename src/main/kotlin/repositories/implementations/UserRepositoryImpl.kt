package repositories.implementations

import com.myclassroom.data.models.User
import tables.UsersTable
import com.myclassroom.db.dbQuery
import com.myclassroom.repositories.repositories.UserRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UserRepositoryImpl : UserRepository {

    override suspend fun createUser(user: User): Boolean = dbQuery {
        UsersTable.insert {
            it[fullName] = user.fullName
            it[userName] = user.userName
            it[email] = user.email
            it[password] = user.password
            it[userRole] = user.userRole
            it[phoneNumber] = user.phoneNumber
        }.insertedCount > 0
    }

    override suspend fun findUserByEmail(email: String?): User? = dbQuery {
        UsersTable.select { UsersTable.email eq email }
            .mapNotNull { row ->
                User(
                    userId = row[UsersTable.userId],
                    fullName = row[UsersTable.fullName] ?: "",
                    userName = row[UsersTable.userName] ?: "",
                    email = row[UsersTable.email] ?: return@mapNotNull null,
                    password = row[UsersTable.password] ?: return@mapNotNull null,
                    userRole = row[UsersTable.userRole] ?: return@mapNotNull null,
                    phoneNumber = row[UsersTable.phoneNumber],
                    createdAt = row[UsersTable.createdAt]
                )
            }
            .singleOrNull()
    }

    override suspend fun getUserById(userId: Long): User? = dbQuery {
        UsersTable.select { UsersTable.userId eq userId }
            .singleOrNull()?.let { row ->
                User(
                    userId = row[UsersTable.userId],
                    fullName = row[UsersTable.fullName],
                    userName = row[UsersTable.userName],
                    email = row[UsersTable.email],
                    phoneNumber = row[UsersTable.phoneNumber],
                    password = null, // Exclude password for security
                    userRole = row[UsersTable.userRole],
                    createdAt = row[UsersTable.createdAt]
                )
            }
    }}