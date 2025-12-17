package com.myclassroom.db

import com.myclassroom.tables.EnrolledStudentsTable
import tables.UsersTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import tables.Classrooms

object DatabaseFactory {
    fun init() {
        var dbUrl = System.getenv("DATABASE_URL")
        if (dbUrl != null && !dbUrl.startsWith("jdbc:")) {
            dbUrl = dbUrl.replace("postgres://", "jdbc:postgresql://")
                .replace("postgresql://", "jdbc:postgresql://")
        }
        dbUrl = dbUrl ?: "jdbc:postgresql://localhost:5432/classloom"
        val dbUser = System.getenv("DB_USER") ?: "postgres"
        val dbPassword = System.getenv("DB_PASSWORD") ?: "123"
        
        Database.connect(
            url = dbUrl,
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword
        )
        transaction {
            SchemaUtils.createMissingTablesAndColumns(UsersTable)
            SchemaUtils.createMissingTablesAndColumns(Classrooms)
            SchemaUtils.createMissingTablesAndColumns(EnrolledStudentsTable)

        }
    }
}suspend fun <T> dbQuery(block: () -> T): T = withContext(Dispatchers.IO) { transaction { block() } }

