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
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/classloom",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "123"
        )
        transaction {
            SchemaUtils.createMissingTablesAndColumns(UsersTable)
            SchemaUtils.createMissingTablesAndColumns(Classrooms)
            SchemaUtils.createMissingTablesAndColumns(EnrolledStudentsTable)

        }
    }
}suspend fun <T> dbQuery(block: () -> T): T = withContext(Dispatchers.IO) { transaction { block() } }

