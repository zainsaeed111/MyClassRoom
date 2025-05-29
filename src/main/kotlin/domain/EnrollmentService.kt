package com.myclassroom.domain

import com.myclassroom.data.base.BaseResponse
import com.myclassroom.data.models.User
import com.myclassroom.repositories.repositories.EnrolledStudentsRepository

class EnrollmentService(
    private val enrollmentRepository: EnrolledStudentsRepository
) {
    suspend fun joinClassByCode(classCode: String, studentId: Long): BaseResponse<Boolean> {
        return enrollmentRepository.joinClassByCode(classCode, studentId)
    }

    suspend fun getEnrolledStudents(classId: Long, offset: Long = 0, limit: Int = 20): BaseResponse<List<User>> {
        return enrollmentRepository.getEnrolledStudents(classId, offset, limit)
    }
}