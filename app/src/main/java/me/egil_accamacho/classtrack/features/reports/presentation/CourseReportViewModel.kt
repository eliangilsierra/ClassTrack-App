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
import me.egil_accamacho.classtrack.features.reports.domain.usecase.GetCourseReportUseCase
import me.egil_accamacho.classtrack.navigation.Destination
import javax.inject.Inject

@HiltViewModel
class CourseReportViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCourseReportUseCase: GetCourseReportUseCase,
) : ViewModel() {

    private val courseId: Long = savedStateHandle.toRoute<Destination.CourseReport>().courseId

    private val _state = MutableStateFlow(CourseReportUiState())
    val state: StateFlow<CourseReportUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun onEvent(event: CourseReportEvent) {
        when (event) {
            CourseReportEvent.Refresh     -> load()
            CourseReportEvent.DismissError -> _state.update { it.copy(error = null) }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null) }
            when (val r = getCourseReportUseCase(courseId)) {
                is Resource.Success -> _state.update { it.copy(loading = false, report = r.data) }
                is Resource.Error   -> _state.update { it.copy(loading = false, error = r.error.message) }
                Resource.Loading    -> Unit
            }
        }
    }
}
