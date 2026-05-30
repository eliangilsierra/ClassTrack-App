package me.egil_accamacho.classtrack.features.courses.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LinkStudentRequest(val studentId: Long)
