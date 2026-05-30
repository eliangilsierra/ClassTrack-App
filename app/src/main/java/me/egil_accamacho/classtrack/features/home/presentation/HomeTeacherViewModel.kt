package me.egil_accamacho.classtrack.features.home.presentation

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
import me.egil_accamacho.classtrack.features.courses.domain.usecase.GetCoursesUseCase
import me.egil_accamacho.classtrack.features.profile.domain.usecase.GetProfileUseCase
import javax.inject.Inject

@HiltViewModel
class HomeTeacherViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val getCoursesUseCase: GetCoursesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeTeacherUiState())
    val state: StateFlow<HomeTeacherUiState> = _state.asStateFlow()

    private val _actions = MutableSharedFlow<HomeTeacherAction>()
    val actions: SharedFlow<HomeTeacherAction> = _actions.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: HomeTeacherEvent) {
        when (event) {
            HomeTeacherEvent.Load                     -> load()
            HomeTeacherEvent.NavigateToCreateCourse   -> emit(HomeTeacherAction.NavigateToCreateCourse)
            is HomeTeacherEvent.NavigateToCourseDetail -> emit(HomeTeacherAction.NavigateToCourseDetail(event.courseId))
            HomeTeacherEvent.NavigateToCourses        -> emit(HomeTeacherAction.NavigateToCourses)
            HomeTeacherEvent.DismissError             -> _state.update { it.copy(error = null) }
        }
    }

    private fun load() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            // Load profile name and courses in parallel
            launch {
                when (val r = getProfileUseCase()) {
                    is Resource.Success -> _state.update { it.copy(teacherName = r.data.fullName) }
                    else -> Unit
                }
            }
            when (val r = getCoursesUseCase()) {
                is Resource.Success -> _state.update { it.copy(loading = false, courses = r.data) }
                is Resource.Error   -> _state.update { it.copy(loading = false, error = r.error.message) }
                Resource.Loading    -> Unit
            }
        }
    }

    private fun emit(action: HomeTeacherAction) {
        viewModelScope.launch { _actions.emit(action) }
    }
}
