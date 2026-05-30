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
import me.egil_accamacho.classtrack.features.profile.domain.usecase.GetProfileUseCase
import javax.inject.Inject

@HiltViewModel
class HomeStudentViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeStudentUiState())
    val state: StateFlow<HomeStudentUiState> = _state.asStateFlow()

    private val _actions = MutableSharedFlow<HomeStudentAction>()
    val actions: SharedFlow<HomeStudentAction> = _actions.asSharedFlow()

    init {
        load()
    }

    fun onEvent(event: HomeStudentEvent) {
        when (event) {
            HomeStudentEvent.Load              -> load()
            HomeStudentEvent.NavigateToScanner -> emit(HomeStudentAction.NavigateToScanner)
            HomeStudentEvent.DismissError      -> _state.update { it.copy(error = null) }
        }
    }

    private fun load() {
        viewModelScope.launch {
            when (val r = getProfileUseCase()) {
                is Resource.Success -> _state.update { it.copy(studentName = r.data.fullName) }
                else -> Unit
            }
        }
    }

    private fun emit(action: HomeStudentAction) {
        viewModelScope.launch { _actions.emit(action) }
    }
}
