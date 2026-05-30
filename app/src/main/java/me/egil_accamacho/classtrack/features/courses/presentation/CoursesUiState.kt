package me.egil_accamacho.classtrack.features.courses.presentation

import me.egil_accamacho.classtrack.features.courses.domain.model.Course

// ── Courses list ──────────────────────────────────────────────────────────────

data class CoursesUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val courses: List<Course> = emptyList(),
    val showDeleteDialog: Boolean = false,
    val courseToDelete: Course? = null,
)

sealed interface CoursesEvent {
    data object Load : CoursesEvent
    data object NavigateToCreate : CoursesEvent
    data class NavigateToDetail(val courseId: Long) : CoursesEvent
    data class RequestDelete(val course: Course) : CoursesEvent
    data object ConfirmDelete : CoursesEvent
    data object DismissDelete : CoursesEvent
    data object DismissError : CoursesEvent
}

sealed interface CoursesAction {
    data object NavigateToCreate : CoursesAction
    data class NavigateToDetail(val courseId: Long) : CoursesAction
}

// ── Create course ─────────────────────────────────────────────────────────────

data class CreateCourseUiState(
    val name: String = "",
    val description: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val nameError: String? = null,
)

sealed interface CreateCourseEvent {
    data class NameChanged(val value: String) : CreateCourseEvent
    data class DescriptionChanged(val value: String) : CreateCourseEvent
    data object Submit : CreateCourseEvent
    data object DismissError : CreateCourseEvent
}

sealed interface CreateCourseAction {
    data class NavigateToDetail(val courseId: Long) : CreateCourseAction
}

// ── Course detail ─────────────────────────────────────────────────────────────

data class CourseDetailUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val course: me.egil_accamacho.classtrack.features.courses.domain.model.Course? = null,
    val students: List<me.egil_accamacho.classtrack.features.courses.domain.model.StudentSummary> = emptyList(),
    val studentsLoading: Boolean = false,
    val showDeleteDialog: Boolean = false,
)

sealed interface CourseDetailEvent {
    data object Load : CourseDetailEvent
    data object NavigateToLinkStudents : CourseDetailEvent
    data object NavigateToCreateSession : CourseDetailEvent
    data object NavigateToReport : CourseDetailEvent
    data object RequestDelete : CourseDetailEvent
    data object ConfirmDelete : CourseDetailEvent
    data object DismissDelete : CourseDetailEvent
    data object DismissError : CourseDetailEvent
}

sealed interface CourseDetailAction {
    data class NavigateToLinkStudents(val courseId: Long) : CourseDetailAction
    data class NavigateToCreateSession(val courseId: Long) : CourseDetailAction
    data class NavigateToReport(val courseId: Long) : CourseDetailAction
    data object NavigateBack : CourseDetailAction
}
