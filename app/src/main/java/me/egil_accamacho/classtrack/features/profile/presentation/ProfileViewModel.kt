package me.egil_accamacho.classtrack.features.profile.presentation

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
import me.egil_accamacho.classtrack.core.qr.QrEncoder
import me.egil_accamacho.classtrack.features.auth.domain.usecase.LogoutUseCase
import me.egil_accamacho.classtrack.features.profile.domain.usecase.GetDigitalIdUseCase
import me.egil_accamacho.classtrack.features.profile.domain.usecase.GetProfileUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val getDigitalIdUseCase: GetDigitalIdUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val qrEncoder: QrEncoder,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    private val _actions = MutableSharedFlow<ProfileAction>()
    val actions: SharedFlow<ProfileAction> = _actions.asSharedFlow()

    init {
        loadProfile()
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.LoadDigitalId      -> loadDigitalId()
            ProfileEvent.ShowLogoutDialog   -> _state.update { it.copy(showLogoutDialog = true) }
            ProfileEvent.DismissLogoutDialog -> _state.update { it.copy(showLogoutDialog = false) }
            ProfileEvent.ConfirmLogout      -> logout()
            ProfileEvent.DismissError       -> _state.update { it.copy(error = null) }
            ProfileEvent.NavigateToDigitalId -> emit(ProfileAction.NavigateToDigitalId)
        }
    }

    // ── Private ───────────────────────────────────────────────────────────────

    private fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            when (val result = getProfileUseCase()) {
                is Resource.Success -> _state.update { it.copy(loading = false, profile = result.data) }
                is Resource.Error   -> _state.update { it.copy(loading = false, error = result.error.message) }
                Resource.Loading    -> Unit
            }
        }
    }

    private fun loadDigitalId() {
        if (_state.value.digitalId != null) return // already loaded
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            when (val result = getDigitalIdUseCase()) {
                is Resource.Success -> {
                    val digitalId = result.data
                    _state.update { it.copy(loading = false, digitalId = digitalId) }
                    qrEncoder.encode(digitalId.qrContent).onSuccess { bitmap ->
                        _state.update { it.copy(qrBitmap = bitmap) }
                    }.onFailure {
                        _state.update { it.copy(error = "No se pudo generar el código QR") }
                    }
                }
                is Resource.Error -> _state.update { it.copy(loading = false, error = result.error.message) }
                Resource.Loading  -> Unit
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            _state.update { it.copy(showLogoutDialog = false) }
            logoutUseCase()
            emit(ProfileAction.NavigateToLogin)
        }
    }

    private fun emit(action: ProfileAction) {
        viewModelScope.launch { _actions.emit(action) }
    }
}
