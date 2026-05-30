package me.egil_accamacho.classtrack.features.courses.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateCourseRequest(
    val name: String,
    val description: String,
)
