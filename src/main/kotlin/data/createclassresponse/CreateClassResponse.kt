package com.myclassroom.data.createclassresponse

import com.myclassroom.data.models.Classroom
import kotlinx.serialization.Serializable

@Serializable
data class CreateClassResponse(
    val classId: Long,
    val classTitle: String,
    val classDescription: String? = null,
    val classSubject: String,
    val classSection: String?,
    val classImageUrl: String? = null,
    val classCode: String,
    val createdBy: String,
    val createdAt: String
) {
    companion object {
        fun fromClassroom(classroom: Classroom): CreateClassResponse {
            return CreateClassResponse(
                classId = classroom.classId,
                classTitle = classroom.classTitle,
                classDescription = classroom.classDescription,
                classSubject = classroom.classSubject,
                classSection = classroom.classSection,
                classImageUrl = classroom.classImageUrl,
                classCode = classroom.classCode,
                createdBy = classroom.createdBy.toString(),
                createdAt = classroom.createdAt.toString()
            )
        }
    }
}
