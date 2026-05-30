package me.egil_accamacho.classtrack.features.attendance.domain.model

data class AttendanceRecord(
    val studentId: Long,
    val studentName: String,
    val registeredAt: String,
)
