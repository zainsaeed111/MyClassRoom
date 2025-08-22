package repositories.implementations

import com.myclassroom.data.authresponse.AuthSubscription
import com.myclassroom.data.models.User
import com.myclassroom.db.dbQuery
import com.myclassroom.repositories.repositories.UserRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import tables.UsersTable
import kotlin.text.set

class UserRepositoryImpl : UserRepository {

    override suspend fun createUser(user: User): Boolean = dbQuery {
        UsersTable.insert {
            it[fullName] = user.fullName
            it[userName] = user.userName
            it[email] = user.email
            it[password] = user.password
            it[userRole] = user.userRole
            it[phoneNumber] = user.phoneNumber

            // Subscription fields
            user.subscription?.let { sub ->
                it[plan] = sub.plan
                it[startDate] = sub.startDate
                it[endDate] = sub.endDate
                it[isActive] = sub.isActive
            }
            it[profilePic] = user.profilePic
            it[organizationName] = user.organizationName
            it[organizationEmail] = user.organizationEmail
            it[organizationType] = user.organizationType
        }.insertedCount > 0
    }

    override suspend fun findUserByEmail(email: String?): User? = dbQuery {
        UsersTable.select { UsersTable.email eq email }
            .mapNotNull { row ->
                User(
                    userId = row[UsersTable.userId],
                    fullName = row[UsersTable.fullName],
                    userName = row[UsersTable.userName],
                    email = row[UsersTable.email],
                    password = row[UsersTable.password],
                    userRole = row[UsersTable.userRole],
                    phoneNumber = row[UsersTable.phoneNumber],
                    createdAt = row[UsersTable.createdAt],
                    organizationName = row[UsersTable.organizationName],
                    organizationEmail = row[UsersTable.organizationEmail],
                    organizationType = row[UsersTable.organizationType],
                    profilePic = row[UsersTable.profilePic],
                    subscription = AuthSubscription(
                        plan = row[UsersTable.plan],
                        startDate = row[UsersTable.startDate],
                        endDate = row[UsersTable.endDate],
                        isActive = row[UsersTable.isActive]
                    )
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
                    password = null, // hide password for safety
                    userRole = row[UsersTable.userRole],
                    createdAt = row[UsersTable.createdAt],
                    organizationName = row[UsersTable.organizationName],
                    organizationEmail = row[UsersTable.organizationEmail],
                    organizationType = row[UsersTable.organizationType],
                    profilePic = row[UsersTable.profilePic],
                    subscription = AuthSubscription(
                        plan = row[UsersTable.plan],
                        startDate = row[UsersTable.startDate],
                        endDate = row[UsersTable.endDate],
                        isActive = row[UsersTable.isActive]
                    )
                )
            }
    }
}
