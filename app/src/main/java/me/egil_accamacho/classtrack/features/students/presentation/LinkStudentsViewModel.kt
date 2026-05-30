package me.egil_accamacho.classtrack.features.students.presentation

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
import me.egil_accamacho.classtrack.features.courses.domain.usecase.GetCourseDetailUseCase
import me.egil_accamacho.classtrack.navigation.Destination
import javax.inject.Inject

@HiltViewModel
class LinkStudentsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCourseDetailUseCase: GetCourseDetailUseCase,
) : ViewModel() {

    private val courseId: Long = savedStateHandle.toRoute<Destination.LinkStudents>().courseId

    private val _state = MutableStateFlow(LinkStudentsUiState())
    val state: StateFlow<LinkStudentsUiState> = _state.asStateFlow()

    private val _actions = MutableSharedFlow<LinkStudentsAction>()
    val actions: SharedFlow<LinkStudentsAction> = _actions.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: LinkStudentsEvent) {
        when (event) {
            LinkStudentsEvent.NavigateToScan -> emit(LinkStudentsAction.NavigateToScan(courseId))
            LinkStudentsEvent.DismissError   -> _state.update { it.copy(error = null) }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            when (val r = getCourseDetailUseCase(courseId)) {
                is Resource.Success -> _state.update {
                    it.copy(loading = false, courseName = r.data.name, studentCount = r.data.studentCount)
                }
                is Resource.Error   -> _state.update { it.copy(loading = false, error = r.error.message) }
                Resource.Loading    -> Unit
            }
        }
    }

    private fun emit(action: LinkStudentsAction) {
        viewModelScope.launch { _actions.emit(action) }
    }
}
