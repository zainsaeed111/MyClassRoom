package com.myclassroom.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import tables.Classrooms
import tables.UsersTable

object EnrolledStudentsTable: Table("enrolled_students") {
    val classId=long("class_id").references(Classrooms.classId, onDelete = ReferenceOption.CASCADE)
    val studentId=long("student_id").references(UsersTable.userId, onDelete = ReferenceOption.CASCADE)
    override val primaryKey = PrimaryKey(classId,studentId)
}