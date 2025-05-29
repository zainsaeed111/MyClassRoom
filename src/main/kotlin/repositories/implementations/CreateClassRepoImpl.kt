package com.myclassroom.repository.implementation

import com.myclassroom.data.enums.UserRole
import com.myclassroom.data.models.Classroom
import com.myclassroom.data.models.User
import com.myclassroom.db.dbQuery
import com.myclassroom.repositories.repositories.CreateClassRepository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import tables.Classrooms
import tables.UsersTable
import java.time.LocalDateTime

class CreateClassRepoImpl : CreateClassRepository {

    override suspend fun createClass(classroom: Classroom): Boolean = dbQuery {
        Classrooms.insert {
            it[classTitle] = classroom.classTitle
            it[classDescription] = classroom.classDescription
            it[classSubject] = classroom.classSubject
            it[classSection] = classroom.classSection
            it[classImageUrl] = classroom.classImageUrl
            it[classCode] = classroom.classCode
            it[createdBy] = classroom.createdBy.userId!!
            it[createdAt] = classroom.createdAt ?: LocalDateTime.now()
        }.insertedCount > 0
    }

    override suspend fun deleteClass(classId: Long): Boolean = dbQuery {
        Classrooms.deleteWhere { Classrooms.classId eq classId } > 0
    }

    override suspend fun getClassByCode(classCode: String): Classroom? = dbQuery {
        (Classrooms innerJoin UsersTable)
            .select { Classrooms.classCode eq classCode }
            .singleOrNull()?.toClassroom()
    }



    override suspend fun getAllClasses(): List<Classroom> = dbQuery {
        (Classrooms innerJoin UsersTable)
            .selectAll()
            .map { it.toClassroom() }
    }

    override suspend fun getClassById(classId: Long): Classroom? {
            return null
    }

    override suspend fun getClassByTitleAndSubject(title: String, subject: String): Classroom? =
        dbQuery {  // Use dbQuery for consistency
            (Classrooms innerJoin UsersTable)  // Add this join
                .select {
                    (Classrooms.classTitle eq title) and
                            (Classrooms.classSubject eq subject)
                }
                .limit(1)
                .singleOrNull()
                ?.toClassroom()
        }

    private fun ResultRow.toClassroom(): Classroom {
        return Classroom(
            classId = this[Classrooms.classId],
            classTitle = this[Classrooms.classTitle],
            classDescription = this[Classrooms.classDescription],
            classSubject = this[Classrooms.classSubject],
            classSection = this[Classrooms.classSection],
            classImageUrl = this[Classrooms.classImageUrl],
            classCode = this[Classrooms.classCode],
            createdBy = User(
                userId = this[UsersTable.userId],
                fullName = this[UsersTable.fullName],
                userName = this[UsersTable.userName],
                email = this[UsersTable.email],
                phoneNumber = this[UsersTable.phoneNumber],
                password = null,
                userRole = this[UsersTable.userRole],
                createdAt = this[UsersTable.createdAt]
            ),
            createdAt = this[Classrooms.createdAt]
        )
    }
}