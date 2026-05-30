package me.egil_accamacho.classtrack.features.profile.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val id: Long,
    val fullName: String,
    val email: String,
    val role: String,
)
