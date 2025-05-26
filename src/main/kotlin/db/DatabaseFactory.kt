package com.myclassroom.db

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
            url = "jdbc:postgresql://localhost:5432/MyClassRoom",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "12345"
        )
        transaction {
            SchemaUtils.create(UsersTable)
            SchemaUtils.createMissingTablesAndColumns(Classrooms)

        }
    }
}suspend fun <T> dbQuery(block: () -> T): T = withContext(Dispatchers.IO) { transaction { block() } }

