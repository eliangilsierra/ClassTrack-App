package me.egil_accamacho.classtrack.features.reports.domain.model

data class CourseReport(
    val courseId: Long,
    val courseName: String,
    val totalStudents: Int,
    val attendanceRate: Int,
)
