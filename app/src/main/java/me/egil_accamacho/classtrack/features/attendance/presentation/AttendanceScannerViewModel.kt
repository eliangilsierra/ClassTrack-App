package me.egil_accamacho.classtrack.features.attendance.presentation

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
import me.egil_accamacho.classtrack.core.location.LocationProvider
import me.egil_accamacho.classtrack.core.qr.QrParser
import me.egil_accamacho.classtrack.core.qr.QrPayload
import me.egil_accamacho.classtrack.features.attendance.domain.usecase.CheckInAttendanceUseCase
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class AttendanceScannerViewModel @Inject constructor(
    private val checkInAttendanceUseCase: CheckInAttendanceUseCase,
    private val locationProvider: LocationProvider,
) : ViewModel() {

    private val _state = MutableStateFlow(AttendanceScannerUiState())
    val state: StateFlow<AttendanceScannerUiState> = _state.asStateFlow()

    private val _actions = MutableSharedFlow<AttendanceScannerAction>()
    val actions: SharedFlow<AttendanceScannerAction> = _actions.asSharedFlow()

    private val isProcessing = AtomicBoolean(false)

    fun onEvent(event: AttendanceScannerEvent) {
        when (event) {
            is AttendanceScannerEvent.QrDetected -> processQr(event.content)
            AttendanceScannerEvent.DismissError  -> {
                _state.update { it.copy(error = null) }
                isProcessing.set(false)
            }
        }
    }

    private fun processQr(content: String) {
        if (!isProcessing.compareAndSet(false, true)) return
        _state.update { it.copy(processing = true) }

        when (val payload = QrParser.parse(content)) {
            is QrPayload.Attendance -> checkIn(payload.sessionId)
            is QrPayload.User -> {
                _state.update { it.copy(processing = false, error = "Este QR es de identificación, no de una sesión de asistencia") }
                isProcessing.set(false)
            }
            QrPayload.Unknown -> {
                _state.update { it.copy(processing = false, error = "QR no reconocido. Pide al profesor que muestre el QR de la sesión") }
                isProcessing.set(false)
            }
        }
    }

    private fun checkIn(sessionId: Long) {
        viewModelScope.launch {
            val location = locationProvider.getLocation()
            if (location == null) {
                _state.update { it.copy(processing = false, error = "No se pudo obtener la ubicación. Verifica que el GPS esté activo") }
                isProcessing.set(false)
                return@launch
            }
            val (lat, lng) = location
            when (val result = checkInAttendanceUseCase(sessionId, lat, lng)) {
                is Resource.Success -> {
                    _state.update { it.copy(processing = false) }
                    _actions.emit(
                        AttendanceScannerAction.NavigateToSuccess(
                            sessionId    = sessionId,
                            registeredAt = result.data.registeredAt,
                        ),
                    )
                }
                is Resource.Error -> {
                    _state.update { it.copy(processing = false, error = result.error.message) }
                    isProcessing.set(false)
                }
                Resource.Loading -> Unit
            }
        }
    }
}
