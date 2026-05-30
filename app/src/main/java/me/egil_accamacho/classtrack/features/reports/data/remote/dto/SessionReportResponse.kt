package me.egil_accamacho.classtrack.features.reports.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SessionReportResponse(
    val sessionId: Long,
    val totalStudents: Int,
    val attendees: Int,
    val absent: Int,
)
