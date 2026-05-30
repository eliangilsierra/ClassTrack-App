package me.egil_accamacho.classtrack.features.profile.domain.model

data class DigitalId(
    val userId: Long,
    val fullName: String,
    val studentCode: String?,
    val qrContent: String,
)
