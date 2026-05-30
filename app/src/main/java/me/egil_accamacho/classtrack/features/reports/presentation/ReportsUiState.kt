package me.egil_accamacho.classtrack.features.reports.presentation

import me.egil_accamacho.classtrack.features.attendance.domain.model.AttendanceRecord
import me.egil_accamacho.classtrack.features.reports.domain.model.CourseReport
import me.egil_accamacho.classtrack.features.reports.domain.model.SessionReport

// ── AttendanceReport (session-level) ─────────────────────────────────────────

data class AttendanceReportUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val report: SessionReport? = null,
    val records: List<AttendanceRecord> = emptyList(),
)

sealed interface AttendanceReportEvent {
    data object Refresh : AttendanceReportEvent
    data object DismissError : AttendanceReportEvent
}

// ── CourseReport ──────────────────────────────────────────────────────────────

data class CourseReportUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val report: CourseReport? = null,
)

sealed interface CourseReportEvent {
    data object Refresh : CourseReportEvent
    data object DismissError : CourseReportEvent
}
