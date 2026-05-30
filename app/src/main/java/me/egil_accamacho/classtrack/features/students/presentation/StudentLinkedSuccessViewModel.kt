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
import me.egil_accamacho.classtrack.features.courses.domain.usecase.GetCourseStudentsUseCase
import me.egil_accamacho.classtrack.navigation.Destination
import javax.inject.Inject

@HiltViewModel
class StudentLinkedSuccessViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCourseStudentsUseCase: GetCourseStudentsUseCase,
) : ViewModel() {

    private val dest = savedStateHandle.toRoute<Destination.StudentLinkedSuccess>()
    private val studentId: Long = dest.studentId
    private val courseId: Long  = dest.courseId

    private val _state = MutableStateFlow(StudentLinkedSuccessUiState(courseId = courseId))
    val state: StateFlow<StudentLinkedSuccessUiState> = _state.asStateFlow()

    private val _actions = MutableSharedFlow<StudentLinkedSuccessAction>()
    val actions: SharedFlow<StudentLinkedSuccessAction> = _actions.asSharedFlow()

    init {
        loadStudent()
    }

    fun onEvent(event: StudentLinkedSuccessEvent) {
        when (event) {
            StudentLinkedSuccessEvent.ScanAnother  -> emit(StudentLinkedSuccessAction.NavigateToScan(courseId))
            StudentLinkedSuccessEvent.BackToCourse -> emit(StudentLinkedSuccessAction.NavigateToCourseDetail(courseId))
        }
    }

    private fun loadStudent() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            when (val r = getCourseStudentsUseCase(courseId)) {
                is Resource.Success -> {
                    val student = r.data.find { it.id == studentId }
                    _state.update { it.copy(loading = false, student = student) }
                }
                else -> _state.update { it.copy(loading = false) }
            }
        }
    }

    private fun emit(action: StudentLinkedSuccessAction) {
        viewModelScope.launch { _actions.emit(action) }
    }
}
