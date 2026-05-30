package me.egil_accamacho.classtrack.features.attendance.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CheckInResponse(
    val attendanceId: Long,
    val registeredAt: String,
)
