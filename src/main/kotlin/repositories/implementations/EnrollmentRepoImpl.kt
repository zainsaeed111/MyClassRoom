package com.myclassroom.repositories.implementations

import com.myclassroom.data.base.BaseResponse
import com.myclassroom.data.enums.UserRole
import com.myclassroom.data.models.User
import com.myclassroom.db.dbQuery
import com.myclassroom.repositories.repositories.EnrolledStudentsRepository
import com.myclassroom.tables.EnrolledStudentsTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.select
import org.slf4j.LoggerFactory
import tables.Classrooms
import tables.UsersTable

class EnrollmentRepoImpl : EnrolledStudentsRepository {
    private val logger = LoggerFactory.getLogger(EnrollmentRepoImpl::class.java)

    override suspend fun joinClassByCode(classCode: String, studentId: Long): BaseResponse<Boolean> = try {
        dbQuery {
            logger.info("Attempting to enroll studentId: $studentId in classCode: $classCode")

            // Explicitly use SqlExpressionBuilder for clarity
            val isValidStudent = UsersTable.select {
                SqlExpressionBuilder.run {
                    (UsersTable.userId eq studentId) and (UsersTable.userRole eq UserRole.STUDENT)
                }
            }.count() > 0

            if (!isValidStudent) {
                logger.warn("Student not found or not a valid student: $studentId")
                return@dbQuery BaseResponse(
                    success = false,
                    message = "Student not found or not a valid student",
                    data = false
                )
            }

            // Fetch classId by classCode
            val classId = Classrooms.select {
                Classrooms.classCode eq classCode
            }.map { it[Classrooms.classId] }.singleOrNull()

            if (classId == null) {
                logger.warn("Class not found for classCode: $classCode")
                return@dbQuery BaseResponse(
                    success = false,
                    message = "Class not found for the given class code",
                    data = false
                )
            }

            // Attempt to enroll student
            val inserted = EnrolledStudentsTable.insertIgnore {
                it[EnrolledStudentsTable.classId] = classId
                it[EnrolledStudentsTable.studentId] = studentId
            }.insertedCount > 0

            if (inserted) {
                logger.info("Successfully enrolled studentId: $studentId in classId: $classId")
                BaseResponse(
                    success = true,
                    message = "Successfully enrolled in class",
                    data = true
                )
            } else {
                logger.info("StudentId: $studentId already enrolled in classId: $classId")
                BaseResponse(
                    success = false,
                    message = "Student is already enrolled in this class",
                    data = false
                )
            }
        }
    } catch (e: Exception) {
        logger.error("Error enrolling studentId: $studentId in classCode: $classCode", e)
        BaseResponse(
            success = false,
            message = "Internal server error: ${e.localizedMessage}",
            data = false
        )
    }

    override suspend fun getEnrolledStudents(classId: Long, offset: Long, limit: Int): BaseResponse<List<User>> = try {
        dbQuery {
            logger.info("Fetching enrolled students for classId: $classId, offset: $offset, limit: $limit")

            // Verify class exists
            val classExists = Classrooms.select {
                Classrooms.classId eq classId
            }.count() > 0

            if (!classExists) {
                logger.warn("Class not found: $classId")
                return@dbQuery BaseResponse(
                    success = false,
                    message = "Class not found",
                    data = emptyList()
                )
            }

            val students = (EnrolledStudentsTable innerJoin UsersTable)
                .select { EnrolledStudentsTable.classId eq classId }
                .limit(limit, offset = offset)
                .map {
                    User(
                        userId = it[UsersTable.userId],
                        fullName = it[UsersTable.fullName],
                        userName = it[UsersTable.userName],
                        email = it[UsersTable.email],
                        phoneNumber = it[UsersTable.phoneNumber],
                        password = null,
                        userRole = it[UsersTable.userRole],
                        createdAt = it[UsersTable.createdAt]
                    )
                }

            logger.info("Retrieved ${students.size} students for classId: $classId")
            BaseResponse(
                success = true,
                message = "Successfully retrieved enrolled students",
                data = students
            )
        }
    } catch (e: Exception) {
        logger.error("Error fetching enrolled students for classId: $classId", e)
        BaseResponse(
            success = false,
            message = "Internal server error: ${e.localizedMessage}",
            data = emptyList()
        )
    }
}