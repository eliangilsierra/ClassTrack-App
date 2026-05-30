package me.egil_accamacho.classtrack.features.attendance.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateSessionRequest(val courseId: Long)
