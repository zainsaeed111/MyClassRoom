package com.myclassroom.domain

import com.myclassroom.data.base.BaseResponse
import com.myclassroom.data.createclassresponse.CreateClassResponse
import com.myclassroom.data.models.Classroom
import com.myclassroom.repositories.repositories.CreateClassRepository
import com.myclassroom.requests.CreateClassRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class ClassroomService(
    private val classroomRepository: CreateClassRepository
) {

    // Create a classroom with validation & duplication check
    suspend fun createClassroom(
        request: CreateClassRequest,
    ): BaseResponse<CreateClassResponse> = withContext(Dispatchers.IO) {
        try {
            // Basic validation
            if (request.classTitle.isBlank()) return@withContext BaseResponse(false, "Class title is required")
            if (request.classSubject.isBlank()) return@withContext BaseResponse(false, "Class subject is required")

            // Generate unique class code
            val classCode = generateUniqueClassCode()

            // Construct domain model
            val classroom = Classroom(
                classTitle = request.classTitle,
                classDescription = request.classDescription,
                classSubject = request.classSubject,
                classSection = request.classSection,
                classImageUrl = request.classCoverImageUrl,
                classCode = classCode,
                createdBy = 0,
                createdAt = LocalDateTime.now()
            )

            // Insert classroom (returns Boolean)
            val inserted = classroomRepository.createClass(classroom)
            if (!inserted) {
                return@withContext BaseResponse(false, "Failed to create classroom")
            }

            // Now fetch the classroom by its unique classCode (assuming it's unique)
            val savedClassroom = classroomRepository.getClassByCode(classCode)
                ?: return@withContext BaseResponse(false, "Failed to retrieve created classroom")

            val response = CreateClassResponse.fromClassroom(savedClassroom)
            BaseResponse(true, "Classroom created successfully", response)

        } catch (e: Exception) {
            BaseResponse(false, "Error creating classroom: ${e.localizedMessage}")
        }
    }

    private suspend fun generateUniqueClassCode(): String {
        // Implement logic to ensure uniqueness
        while (true) {
            val code = (100000..999999).random().toString()
            val exists = classroomRepository.getClassByCode(code) != null
            if (!exists) return code
        }
    }
    // Get classroom by ID with error handling
    suspend fun getClassroomById(classId: Long): BaseResponse<Classroom> = withContext(Dispatchers.IO) {
        try {
            val classroom = classroomRepository.getClassById(classId)
            if (classroom == null) {
                return@withContext BaseResponse(false, "Classroom not found")
            }
            BaseResponse(true, "Classroom fetched successfully", classroom)
        } catch (e: Exception) {
            BaseResponse(false, "Error fetching classroom: ${e.localizedMessage}")
        }
    }

    // Get all classrooms
    suspend fun getAllClassrooms(): BaseResponse<List<Classroom>> = withContext(Dispatchers.IO) {
        try {
            val classrooms = classroomRepository.getAllClasses()
            BaseResponse(true, "Classrooms fetched successfully", classrooms)
        } catch (e: Exception) {
            BaseResponse(false, "Error fetching classrooms: ${e.localizedMessage}")
        }
    }

    // Delete classroom by ID
    suspend fun deleteClassroom(classId: Long): BaseResponse<Unit> = withContext(Dispatchers.IO) {
        try {
            val deleted = classroomRepository.deleteClass(classId)
            if (!deleted) return@withContext BaseResponse(false, "Classroom not found or couldn't be deleted")
            BaseResponse(true, "Classroom deleted successfully")
        } catch (e: Exception) {
            BaseResponse(false, "Error deleting classroom: ${e.localizedMessage}")
        }
    }
}