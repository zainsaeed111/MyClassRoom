package tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object Classrooms : Table("classrooms") {
    val classId = long("class_id").autoIncrement()
    val classTitle = varchar("class_title", 255)
    val classDescription = varchar("class_description", 1000).nullable()
    val classSubject = varchar("class_subject", 255)
    val classSection = varchar("class_section", 100).nullable()
    val classImageUrl = varchar("class_image_url", 500).nullable()
    val classCode = varchar("class_code", 100).uniqueIndex()
    val createdBy = long("created_by").references(UsersTable.userId)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    override val primaryKey = PrimaryKey(classId)
}