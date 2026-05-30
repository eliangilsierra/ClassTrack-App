package me.egil_accamacho.classtrack.features.attendance.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import me.egil_accamacho.classtrack.core.common.Resource
import me.egil_accamacho.classtrack.core.qr.QrEncoder
import me.egil_accamacho.classtrack.features.attendance.domain.usecase.CloseAttendanceSessionUseCase
import me.egil_accamacho.classtrack.features.attendance.domain.usecase.GetAttendanceRecordsUseCase
import me.egil_accamacho.classtrack.navigation.Destination
import javax.inject.Inject

@HiltViewModel
class AttendanceQrViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val qrEncoder: QrEncoder,
    private val closeAttendanceSessionUseCase: CloseAttendanceSessionUseCase,
    private val getAttendanceRecordsUseCase: GetAttendanceRecordsUseCase,
) : ViewModel() {

    private val dest = savedStateHandle.toRoute<Destination.AttendanceQr>()
    private val sessionId: Long      = dest.sessionId
    private val qrToken: String      = dest.qrToken
    private val durationMinutes: Int = dest.durationMinutes

    private val _state = MutableStateFlow(AttendanceQrUiState())
    val state: StateFlow<AttendanceQrUiState> = _state.asStateFlow()

    private val _actions = MutableSharedFlow<AttendanceQrAction>()
    val actions: SharedFlow<AttendanceQrAction> = _actions.asSharedFlow()

    init {
        generateQr()
        startPolling()
    }

    fun onEvent(event: AttendanceQrEvent) {
        when (event) {
            AttendanceQrEvent.CloseSession -> closeSession()
            AttendanceQrEvent.DismissError -> _state.update { it.copy(error = null) }
        }
    }

    private fun generateQr() {
        viewModelScope.launch {
            val content = """{"type":"ATTENDANCE","sessionId":$sessionId,"token":"$qrToken"}"""
            val total = durationMinutes * 60
            val bitmap = qrEncoder.encode(content).getOrNull()
            _state.update {
                it.copy(
                    qrBitmap         = bitmap,
                    remainingSeconds = total,
                    totalSeconds     = total,
                )
            }
        }
    }

    private fun startPolling() {
        viewModelScope.launch {
            while (isActive) {
                pollRecords()
                delay(10_000L)
            }
        }
    }

    private suspend fun pollRecords() {
        when (val r = getAttendanceRecordsUseCase(sessionId)) {
            is Resource.Success -> _state.update { it.copy(records = r.data) }
            else -> Unit
        }
    }

    private fun closeSession() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            when (val r = closeAttendanceSessionUseCase(sessionId)) {
                is Resource.Success -> {
                    _state.update { it.copy(loading = false) }
                    _actions.emit(AttendanceQrAction.NavigateToReport(sessionId))
                }
                is Resource.Error -> _state.update { it.copy(loading = false, error = r.error.message) }
                Resource.Loading  -> Unit
            }
        }
    }

}
