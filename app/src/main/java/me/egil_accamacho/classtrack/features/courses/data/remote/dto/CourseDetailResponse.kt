package me.egil_accamacho.classtrack.features.courses.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CourseDetailResponse(
    val id: Long,
    val name: String,
    val description: String,
    val studentCount: Int,
)
