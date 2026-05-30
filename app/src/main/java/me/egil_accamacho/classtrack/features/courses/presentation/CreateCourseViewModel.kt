package me.egil_accamacho.classtrack.features.courses.presentation

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
import me.egil_accamacho.classtrack.features.courses.domain.usecase.CreateCourseUseCase
import javax.inject.Inject

@HiltViewModel
class CreateCourseViewModel @Inject constructor(
    private val createCourseUseCase: CreateCourseUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CreateCourseUiState())
    val state: StateFlow<CreateCourseUiState> = _state.asStateFlow()

    private val _actions = MutableSharedFlow<CreateCourseAction>()
    val actions: SharedFlow<CreateCourseAction> = _actions.asSharedFlow()

    fun onEvent(event: CreateCourseEvent) {
        when (event) {
            is CreateCourseEvent.NameChanged        -> _state.update { it.copy(name = event.value, nameError = null) }
            is CreateCourseEvent.DescriptionChanged -> _state.update { it.copy(description = event.value) }
            CreateCourseEvent.Submit                -> submit()
            CreateCourseEvent.DismissError          -> _state.update { it.copy(error = null) }
        }
    }

    private fun submit() {
        val name = _state.value.name.trim()
        if (name.isBlank()) {
            _state.update { it.copy(nameError = "El nombre del curso es requerido") }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            when (val result = createCourseUseCase(name, _state.value.description.trim())) {
                is Resource.Success -> {
                    _state.update { it.copy(loading = false) }
                    _actions.emit(CreateCourseAction.NavigateToDetail(result.data.id))
                }
                is Resource.Error   -> _state.update { it.copy(loading = false, error = result.error.message) }
                Resource.Loading    -> Unit
            }
        }
    }
}
