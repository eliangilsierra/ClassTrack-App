package me.egil_accamacho.classtrack.features.profile.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DigitalIdResponse(
    val userId: Long,
    val fullName: String,
    val studentCode: String,
    val qrContent: String,
)
