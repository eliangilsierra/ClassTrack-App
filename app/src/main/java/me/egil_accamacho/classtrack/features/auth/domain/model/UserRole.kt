package me.egil_accamacho.classtrack.features.auth.domain.model

enum class UserRole {
    TEACHER,
    STUDENT;

    val label: String get() = when (this) {
        TEACHER -> "Docente"
        STUDENT -> "Estudiante"
    }
}
