package me.egil_accamacho.classtrack.features.reports.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.egil_accamacho.classtrack.core.common.Resource
import me.egil_accamacho.classtrack.features.attendance.domain.usecase.GetAttendanceRecordsUseCase
import me.egil_accamacho.classtrack.features.reports.domain.usecase.GetSessionReportUseCase
import me.egil_accamacho.classtrack.navigation.Destination
import javax.inject.Inject

@HiltViewModel
class AttendanceReportViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getSessionReportUseCase: GetSessionReportUseCase,
    private val getAttendanceRecordsUseCase: GetAttendanceRecordsUseCase,
) : ViewModel() {

    private val sessionId: Long = savedStateHandle.toRoute<Destination.AttendanceReport>().sessionId

    private val _state = MutableStateFlow(AttendanceReportUiState())
    val state: StateFlow<AttendanceReportUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun onEvent(event: AttendanceReportEvent) {
        when (event) {
            AttendanceReportEvent.Refresh     -> load()
            AttendanceReportEvent.DismissError -> _state.update { it.copy(error = null) }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }

            val reportResult  = getSessionReportUseCase(sessionId)
            val recordsResult = getAttendanceRecordsUseCase(sessionId)

            _state.update { current ->
                current.copy(
                    loading = false,
                    report  = if (reportResult is Resource.Success) reportResult.data else current.report,
                    records = if (recordsResult is Resource.Success) recordsResult.data else current.records,
                    error   = when {
                        reportResult is Resource.Error  -> reportResult.error.message
                        recordsResult is Resource.Error -> recordsResult.error.message
                        else -> null
                    },
                )
            }
        }
    }
}
