package me.egil_accamacho.classtrack.features.attendance.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CheckInRequest(
    val sessionId: Long,
    val latitude: Double,
    val longitude: Double,
)
