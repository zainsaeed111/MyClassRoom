package tables

import com.myclassroom.data.enums.UserRole
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object UsersTable : Table("users") {
    val userId = long("user_id").autoIncrement()
    val fullName = varchar("full_name", 100).nullable()
    val userName = varchar("user_name", 100).nullable()
    val email = varchar("email", 100).nullable()
    val phoneNumber = varchar("phone_number", 15).nullable()
    val password = varchar("password", 100).nullable()
    val userRole = enumerationByName("user_role", 20, UserRole::class).nullable()
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime) // ✅ use singleton
    override val primaryKey = PrimaryKey(userId)
}