package me.egil_accamacho.classtrack.features.attendance.presentation

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
import me.egil_accamacho.classtrack.features.attendance.domain.usecase.CreateAttendanceSessionUseCase
import me.egil_accamacho.classtrack.features.courses.domain.usecase.GetCourseDetailUseCase
import me.egil_accamacho.classtrack.navigation.Destination
import javax.inject.Inject

@HiltViewModel
class CreateAttendanceSessionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCourseDetailUseCase: GetCourseDetailUseCase,
    private val createAttendanceSessionUseCase: CreateAttendanceSessionUseCase,
) : ViewModel() {

    private val courseId: Long = savedStateHandle.toRoute<Destination.CreateAttendanceSession>().courseId

    private val _state = MutableStateFlow(CreateAttendanceSessionUiState())
    val state: StateFlow<CreateAttendanceSessionUiState> = _state.asStateFlow()

    private val _actions = MutableSharedFlow<CreateAttendanceSessionAction>()
    val actions: SharedFlow<CreateAttendanceSessionAction> = _actions.asSharedFlow()

    init {
        loadCourseName()
    }

    fun onEvent(event: CreateAttendanceSessionEvent) {
        when (event) {
            CreateAttendanceSessionEvent.CreateSession        -> createSession()
            CreateAttendanceSessionEvent.DismissError         -> _state.update { it.copy(error = null) }
            is CreateAttendanceSessionEvent.DurationChanged   -> _state.update { it.copy(durationMinutes = event.minutes) }
        }
    }

    private fun loadCourseName() {
        viewModelScope.launch {
            when (val r = getCourseDetailUseCase(courseId)) {
                is Resource.Success -> _state.update { it.copy(courseName = r.data.name) }
                else -> Unit
            }
        }
    }

    private fun createSession() {
        viewModelScope.launch {
            val duration = _state.value.durationMinutes
            _state.update { it.copy(loading = true) }
            when (val r = createAttendanceSessionUseCase(courseId, duration)) {
                is Resource.Success -> {
                    _state.update { it.copy(loading = false) }
                    _actions.emit(
                        CreateAttendanceSessionAction.NavigateToQr(
                            sessionId       = r.data.sessionId,
                            qrToken         = r.data.qrToken,
                            expiresAt       = r.data.expiresAt,
                            durationMinutes = duration,
                        ),
                    )
                }
                is Resource.Error -> _state.update { it.copy(loading = false, error = r.error.message) }
                Resource.Loading  -> Unit
            }
        }
    }
}
