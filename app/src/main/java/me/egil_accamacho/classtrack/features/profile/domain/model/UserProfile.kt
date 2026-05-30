package me.egil_accamacho.classtrack.features.profile.domain.model

data class UserProfile(
    val id: Long,
    val fullName: String,
    val email: String,
    val role: String,
) {
    val isTeacher: Boolean get() = role.uppercase() == "TEACHER"
    val roleLabel: String get() = if (isTeacher) "Docente" else "Estudiante"
}
