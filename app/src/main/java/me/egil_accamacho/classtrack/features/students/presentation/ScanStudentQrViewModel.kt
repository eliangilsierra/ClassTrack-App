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
import me.egil_accamacho.classtrack.core.qr.QrParser
import me.egil_accamacho.classtrack.core.qr.QrPayload
import me.egil_accamacho.classtrack.features.courses.domain.usecase.LinkStudentToCourseUseCase
import me.egil_accamacho.classtrack.navigation.Destination
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class ScanStudentQrViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val linkStudentToCourseUseCase: LinkStudentToCourseUseCase,
) : ViewModel() {

    private val courseId: Long = savedStateHandle.toRoute<Destination.ScanStudentQr>().courseId

    private val _state = MutableStateFlow(ScanStudentQrUiState())
    val state: StateFlow<ScanStudentQrUiState> = _state.asStateFlow()

    private val _actions = MutableSharedFlow<ScanStudentQrAction>()
    val actions: SharedFlow<ScanStudentQrAction> = _actions.asSharedFlow()

    private val isProcessing = AtomicBoolean(false)

    fun onEvent(event: ScanStudentQrEvent) {
        when (event) {
            is ScanStudentQrEvent.QrDetected -> processQr(event.content)
            ScanStudentQrEvent.DismissError  -> {
                _state.update { it.copy(error = null) }
                isProcessing.set(false)
            }
        }
    }

    private fun processQr(content: String) {
        if (!isProcessing.compareAndSet(false, true)) return
        _state.update { it.copy(processing = true) }

        when (val payload = QrParser.parse(content)) {
            is QrPayload.User -> linkStudent(payload.userId)
            is QrPayload.Attendance -> {
                _state.update { it.copy(processing = false, error = "Este QR es de asistencia, no de un estudiante") }
                isProcessing.set(false)
            }
            QrPayload.Unknown -> {
                _state.update { it.copy(processing = false, error = "QR no reconocido. Pide al estudiante que muestre su identificación digital") }
                isProcessing.set(false)
            }
        }
    }

    private fun linkStudent(studentId: Long) {
        viewModelScope.launch {
            when (val result = linkStudentToCourseUseCase(courseId, studentId)) {
                is Resource.Success -> {
                    _state.update { it.copy(processing = false) }
                    _actions.emit(ScanStudentQrAction.NavigateToSuccess(studentId, courseId))
                }
                is Resource.Error   -> {
                    _state.update { it.copy(processing = false, error = result.error.message) }
                    isProcessing.set(false)
                }
                Resource.Loading    -> Unit
            }
        }
    }
}
