package me.egil_accamacho.classtrack.features.reports.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CourseReportResponse(
    val courseId: Long,
    val courseName: String,
    val totalStudents: Int,
    val attendanceRate: Int,
)
