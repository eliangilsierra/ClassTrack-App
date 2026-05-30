package me.egil_accamacho.classtrack.features.attendance.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AttendanceRecordResponse(
    val studentId: Long,
    val studentName: String,
    val registeredAt: String,
)
