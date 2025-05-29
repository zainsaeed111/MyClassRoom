package com.myclassroom.repositories.repositories

import com.myclassroom.data.base.BaseResponse
import com.myclassroom.data.models.User

interface EnrolledStudentsRepository {
    suspend fun joinClassByCode(classCode: String, studentId: Long): BaseResponse<Boolean>
    suspend fun getEnrolledStudents(classId: Long, offset: Long = 0, limit: Int = 20): BaseResponse<List<User>>
}