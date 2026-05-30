package me.egil_accamacho.classtrack.features.auth.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val userId: Long,
    val token: String,
)
