package me.egil_accamacho.classtrack.features.attendance.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AttendanceSessionResponse(
    val sessionId: Long,
    val courseId: Long,
    val qrToken: String,
    val expiresAt: String,
)
