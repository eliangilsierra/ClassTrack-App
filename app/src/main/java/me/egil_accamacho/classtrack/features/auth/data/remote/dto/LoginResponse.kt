package me.egil_accamacho.classtrack.features.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val token: String,
    val userId: Long,
    val role: String,
)
