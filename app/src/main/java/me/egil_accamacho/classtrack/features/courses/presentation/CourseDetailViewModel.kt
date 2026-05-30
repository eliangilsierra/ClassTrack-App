package me.egil_accamacho.classtrack.features.courses.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.egil_accamacho.classtrack.core.common.Resource
import me.egil_accamacho.classtrack.features.courses.domain.usecase.DeleteCourseUseCase
import me.egil_accamacho.classtrack.features.courses.domain.usecase.GetCourseDetailUseCase
import me.egil_accamacho.classtrack.features.courses.domain.usecase.GetCourseStudentsUseCase
import me.egil_accamacho.classtrack.navigation.Destination
import javax.inject.Inject

@HiltViewModel
class CourseDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCourseDetailUseCase: GetCourseDetailUseCase,
    private val getCourseStudentsUseCase: GetCourseStudentsUseCase,
    private val deleteCourseUseCase: DeleteCourseUseCase,
) : ViewModel() {

    private val courseId: Long = savedStateHandle.toRoute<Destination.CourseDetail>().courseId

    private val _state = MutableStateFlow(CourseDetailUiState())
    val state: StateFlow<CourseDetailUiState> = _state.asStateFlow()

    private val _actions = MutableSharedFlow<CourseDetailAction>()
    val actions: SharedFlow<CourseDetailAction> = _actions.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: CourseDetailEvent) {
        when (event) {
            CourseDetailEvent.Load                  -> load()
            CourseDetailEvent.NavigateToLinkStudents -> emit(CourseDetailAction.NavigateToLinkStudents(courseId))
            CourseDetailEvent.NavigateToCreateSession -> emit(CourseDetailAction.NavigateToCreateSession(courseId))
            CourseDetailEvent.NavigateToReport       -> emit(CourseDetailAction.NavigateToReport(courseId))
            CourseDetailEvent.RequestDelete          -> _state.update { it.copy(showDeleteDialog = true) }
            CourseDetailEvent.ConfirmDelete          -> deleteCourse()
            CourseDetailEvent.DismissDelete          -> _state.update { it.copy(showDeleteDialog = false) }
            CourseDetailEvent.DismissError           -> _state.update { it.copy(error = null) }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            when (val result = getCourseDetailUseCase(courseId)) {
                is Resource.Success -> _state.update { it.copy(loading = false, course = result.data) }
                is Resource.Error   -> _state.update { it.copy(loading = false, error = result.error.message) }
                Resource.Loading    -> Unit
            }
        }
        loadStudents()
    }

    private fun loadStudents() {
        viewModelScope.launch {
            _state.update { it.copy(studentsLoading = true) }
            when (val result = getCourseStudentsUseCase(courseId)) {
                is Resource.Success -> _state.update { it.copy(studentsLoading = false, students = result.data) }
                is Resource.Error   -> _state.update { it.copy(studentsLoading = false) }
                Resource.Loading    -> Unit
            }
        }
    }

    private fun deleteCourse() {
        _state.update { it.copy(showDeleteDialog = false) }
        viewModelScope.launch {
            when (deleteCourseUseCase(courseId)) {
                is Resource.Success -> emit(CourseDetailAction.NavigateBack)
                is Resource.Error   -> _state.update { it.copy(error = "No se pudo eliminar el curso") }
                Resource.Loading    -> Unit
            }
        }
    }

    private fun emit(action: CourseDetailAction) {
        viewModelScope.launch { _actions.emit(action) }
    }
}
