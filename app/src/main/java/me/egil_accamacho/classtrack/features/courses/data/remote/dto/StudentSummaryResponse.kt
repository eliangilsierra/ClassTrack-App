package me.egil_accamacho.classtrack.features.courses.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StudentSummaryResponse(
    val id: Long,
    val fullName: String,
    val studentCode: String,
)
