package com.myclassroom.repository.implementation

import com.myclassroom.data.models.Classroom
import com.myclassroom.data.models.User
import com.myclassroom.db.dbQuery
import com.myclassroom.repositories.repositories.CreateClassRepository
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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
            it[createdBy] =classroom.createdBy
            it[createdAt] = classroom.createdAt ?: LocalDateTime.now()
        }.insertedCount > 0
    }

    override suspend fun deleteClass(classId: Long): Boolean = dbQuery {
        Classrooms.deleteWhere { Classrooms.classId eq classId } > 0
    }

    override suspend fun getClassByCode(classCode: String): Classroom? = dbQuery {
        Classrooms.select { Classrooms.classCode eq classCode }.singleOrNull() as Classroom?
    }

    override suspend fun getAllClasses(): List<Classroom> = dbQuery {
        Classrooms.selectAll().map { row ->
            Classroom(
                classId = row[Classrooms.classId],
                classTitle = row[Classrooms.classTitle],
                classDescription = row[Classrooms.classDescription],
                classSubject = row[Classrooms.classSubject],
                classSection = row[Classrooms.classSection],
                classImageUrl = row[Classrooms.classImageUrl],
                classCode = row[Classrooms.classCode],
                createdBy = row[Classrooms.createdBy],
                createdAt = row[Classrooms.createdAt]
            )
        }
    }

    override suspend fun getClassById(classId: Long): Classroom? = dbQuery {
        Classrooms.select { Classrooms.classId eq classId }.singleOrNull()?.let { row ->
            Classroom(
                classId = row[Classrooms.classId],
                classTitle = row[Classrooms.classTitle],
                classDescription = row[Classrooms.classDescription],
                classSubject = row[Classrooms.classSubject],
                classSection = row[Classrooms.classSection],
                classImageUrl = row[Classrooms.classImageUrl],
                classCode = row[Classrooms.classCode],
                createdBy = row[Classrooms.createdBy],
                createdAt = row[Classrooms.createdAt]
            )
        }
    }
}