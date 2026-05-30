package me.egil_accamacho.classtrack.features.courses.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CourseListItemResponse(
    val id: Long,
    val name: String,
    val studentCount: Int,
)
