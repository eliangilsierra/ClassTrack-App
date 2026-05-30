package me.egil_accamacho.classtrack.features.reports.domain.model

data class SessionReport(
    val sessionId: Long,
    val totalStudents: Int,
    val attendees: Int,
    val absent: Int,
) {
    val attendancePercent: Int
        get() = if (totalStudents > 0) (attendees * 100) / totalStudents else 0
}
