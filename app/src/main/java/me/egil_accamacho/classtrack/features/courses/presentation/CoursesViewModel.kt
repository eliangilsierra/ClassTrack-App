package me.egil_accamacho.classtrack.features.courses.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import me.egil_accamacho.classtrack.features.courses.domain.usecase.GetCoursesUseCase
import javax.inject.Inject

@HiltViewModel
class CoursesViewModel @Inject constructor(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val deleteCourseUseCase: DeleteCourseUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CoursesUiState())
    val state: StateFlow<CoursesUiState> = _state.asStateFlow()

    private val _actions = MutableSharedFlow<CoursesAction>()
    val actions: SharedFlow<CoursesAction> = _actions.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: CoursesEvent) {
        when (event) {
            CoursesEvent.Load                    -> load()
            CoursesEvent.NavigateToCreate        -> emit(CoursesAction.NavigateToCreate)
            is CoursesEvent.NavigateToDetail     -> emit(CoursesAction.NavigateToDetail(event.courseId))
            is CoursesEvent.RequestDelete        -> _state.update { it.copy(showDeleteDialog = true, courseToDelete = event.course) }
            CoursesEvent.ConfirmDelete           -> deleteSelected()
            CoursesEvent.DismissDelete           -> _state.update { it.copy(showDeleteDialog = false, courseToDelete = null) }
            CoursesEvent.DismissError            -> _state.update { it.copy(error = null) }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            when (val result = getCoursesUseCase()) {
                is Resource.Success -> _state.update { it.copy(loading = false, courses = result.data) }
                is Resource.Error   -> _state.update { it.copy(loading = false, error = result.error.message) }
                Resource.Loading    -> Unit
            }
        }
    }

    private fun deleteSelected() {
        val course = _state.value.courseToDelete ?: return
        _state.update { it.copy(showDeleteDialog = false, courseToDelete = null) }
        viewModelScope.launch {
            when (val result = deleteCourseUseCase(course.id)) {
                is Resource.Success -> load()
                is Resource.Error   -> _state.update { it.copy(error = result.error.message) }
                Resource.Loading    -> Unit
            }
        }
    }

    private fun emit(action: CoursesAction) {
        viewModelScope.launch { _actions.emit(action) }
    }
}
