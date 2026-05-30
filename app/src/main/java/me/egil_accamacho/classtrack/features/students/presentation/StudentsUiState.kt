package me.egil_accamacho.classtrack.features.students.presentation

import me.egil_accamacho.classtrack.features.courses.domain.model.StudentSummary

// ── LinkStudents ──────────────────────────────────────────────────────────────

data class LinkStudentsUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val courseName: String = "",
    val studentCount: Int = 0,
)

sealed interface LinkStudentsEvent {
    data object NavigateToScan : LinkStudentsEvent
    data object DismissError : LinkStudentsEvent
}

sealed interface LinkStudentsAction {
    data class NavigateToScan(val courseId: Long) : LinkStudentsAction
}

// ── ScanStudentQr ─────────────────────────────────────────────────────────────

data class ScanStudentQrUiState(
    val processing: Boolean = false,
    val error: String? = null,
)

sealed interface ScanStudentQrEvent {
    data class QrDetected(val content: String) : ScanStudentQrEvent
    data object DismissError : ScanStudentQrEvent
}

sealed interface ScanStudentQrAction {
    data class NavigateToSuccess(val studentId: Long, val courseId: Long) : ScanStudentQrAction
}

// ── StudentLinkedSuccess ──────────────────────────────────────────────────────

data class StudentLinkedSuccessUiState(
    val loading: Boolean = false,
    val student: StudentSummary? = null,
    val courseId: Long = 0,
)

sealed interface StudentLinkedSuccessEvent {
    data object ScanAnother : StudentLinkedSuccessEvent
    data object BackToCourse : StudentLinkedSuccessEvent
}

sealed interface StudentLinkedSuccessAction {
    data class NavigateToScan(val courseId: Long) : StudentLinkedSuccessAction
    data class NavigateToCourseDetail(val courseId: Long) : StudentLinkedSuccessAction
}
