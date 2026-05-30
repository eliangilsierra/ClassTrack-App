package me.egil_accamacho.classtrack.features.courses.domain.model

data class Course(
    val id: Long,
    val name: String,
    val description: String = "",
    val studentCount: Int = 0,
)
