package com.myclassroom.domain

import com.myclassroom.data.base.BaseResponse
import com.myclassroom.data.createclassresponse.CreateClassResponse
import com.myclassroom.data.enums.UserRole
import com.myclassroom.data.models.Classroom
import com.myclassroom.data.models.User
import com.myclassroom.repositories.repositories.CreateClassRepository
import com.myclassroom.requests.CreateClassRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class ClassroomService(
    private val classroomRepository: CreateClassRepository
) {
    suspend fun createClassroom(
        request: CreateClassRequest,
        user: User
    ): BaseResponse<CreateClassResponse> = withContext(Dispatchers.IO) {
        try {
            if (user.userRole != UserRole.TEACHER) {
                return@withContext BaseResponse(false, "Only teachers can create classes")
            }
            if (request.classTitle.isBlank()) return@withContext BaseResponse(false, "Class title is required")
            if (request.classSubject.isBlank()) return@withContext BaseResponse(false, "Class subject is required")

            val existingClass = classroomRepository.getClassByTitleAndSubject(
                request.classTitle,
                request.classSubject
            )

            if (existingClass != null) {
                return@withContext BaseResponse(false, "Class with this title and subject already exists")
            }

            val classCode = generateUniqueClassCode()
            val classroom = Classroom(
                classTitle = request.classTitle,
                classDescription = request.classDescription,
                classSubject = request.classSubject,
                classSection = request.classSection,
                classImageUrl = request.classCoverImageUrl,
                classCode = classCode,
                createdBy = user,
                createdAt = LocalDateTime.now()
            )

            val inserted = classroomRepository.createClass(classroom)
            if (!inserted) {
                return@withContext BaseResponse(false, "Failed to create classroom")
            }

            // Fetch the created classroom
            val savedClassroom = classroomRepository.getClassByCode(classCode)
                ?: return@withContext BaseResponse(false, "Failed to retrieve created classroom")

            val response = CreateClassResponse.fromClassroom(savedClassroom)
            BaseResponse(true, "Classroom created successfully", response)
        } catch (e: Exception) {
            BaseResponse(false, "Error creating classroom: ${e.localizedMessage}")
        }
    }

    private suspend fun generateUniqueClassCode(): String {
        while (true) {
            val code = (100000..999999).random().toString()
            val exists = classroomRepository.getClassByCode(code) != null
            if (!exists) return code
        }
    }
}