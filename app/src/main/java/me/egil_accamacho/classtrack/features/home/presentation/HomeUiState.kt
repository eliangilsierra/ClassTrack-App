package me.egil_accamacho.classtrack.features.home.presentation

import me.egil_accamacho.classtrack.features.courses.domain.model.Course

// ── Teacher ───────────────────────────────────────────────────────────────────

data class HomeTeacherUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val teacherName: String = "",
    val courses: List<Course> = emptyList(),
)

sealed interface HomeTeacherEvent {
    data object Load : HomeTeacherEvent
    data object NavigateToCreateCourse : HomeTeacherEvent
    data class NavigateToCourseDetail(val courseId: Long) : HomeTeacherEvent
    data object NavigateToCourses : HomeTeacherEvent
    data object DismissError : HomeTeacherEvent
}

sealed interface HomeTeacherAction {
    data object NavigateToCreateCourse : HomeTeacherAction
    data class NavigateToCourseDetail(val courseId: Long) : HomeTeacherAction
    data object NavigateToCourses : HomeTeacherAction
}

// ── Student ───────────────────────────────────────────────────────────────────

data class HomeStudentUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val studentName: String = "",
)

sealed interface HomeStudentEvent {
    data object Load : HomeStudentEvent
    data object NavigateToScanner : HomeStudentEvent
    data object DismissError : HomeStudentEvent
}

sealed interface HomeStudentAction {
    data object NavigateToScanner : HomeStudentAction
}
