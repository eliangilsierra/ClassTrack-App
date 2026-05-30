package me.egil_accamacho.classtrack.features.attendance.domain.model

data class AttendanceSession(
    val sessionId: Long,
    val courseId: Long,
    val qrToken: String,
    val expiresAt: String,
)
