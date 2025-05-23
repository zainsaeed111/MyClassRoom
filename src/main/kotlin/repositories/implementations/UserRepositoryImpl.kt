package repositories.implementations

import com.myclassroom.data.User
import com.myclassroom.data.tables.UsersTable
import com.myclassroom.db.dbQuery
import com.myclassroom.repositories.UserRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UserRepositoryImpl : UserRepository {

    override suspend fun createUser(user: User): Boolean = dbQuery {
        UsersTable.insert {
//            it[userId] = user.userId ?: 0  // Let auto-increment handle it if null
            it[fullName] = user.fullName
            it[userName] = user.userName
            it[email] = user.email
            it[password] = user.password
            it[userRole] = user.userRole
            it[phoneNumber] = user.phoneNumber
        }.insertedCount > 0
    }

    override suspend fun findUserByEmail(email: String): User? = dbQuery {
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
}