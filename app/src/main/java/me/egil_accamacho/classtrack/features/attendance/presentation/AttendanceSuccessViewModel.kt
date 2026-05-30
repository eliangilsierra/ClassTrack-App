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
import kotlinx.coroutines.launch
import me.egil_accamacho.classtrack.navigation.Destination
import javax.inject.Inject

@HiltViewModel
class AttendanceSuccessViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val dest = savedStateHandle.toRoute<Destination.AttendanceSuccess>()

    private val _state = MutableStateFlow(
        AttendanceSuccessUiState(registeredAt = dest.registeredAt),
    )
    val state: StateFlow<AttendanceSuccessUiState> = _state.asStateFlow()

    private val _actions = MutableSharedFlow<AttendanceSuccessAction>()
    val actions: SharedFlow<AttendanceSuccessAction> = _actions.asSharedFlow()

    fun onEvent(event: AttendanceSuccessEvent) {
        when (event) {
            AttendanceSuccessEvent.GoHome -> viewModelScope.launch {
                _actions.emit(AttendanceSuccessAction.NavigateHome)
            }
        }
    }
}
