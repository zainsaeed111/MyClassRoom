package com.myclassroom.repositories.repositories
import com.myclassroom.data.models.Classroom

interface CreateClassRepository {
    suspend fun createClass(classroom: Classroom): Boolean
    suspend fun deleteClass(classId: Long): Boolean
    suspend fun getAllClasses(): List<Classroom>
    suspend fun getClassById(classId: Long): Classroom?
    suspend fun getClassByCode(classCode: String): Classroom?
    suspend fun getClassByTitleAndSubject(title: String, subject: String): Classroom?




}