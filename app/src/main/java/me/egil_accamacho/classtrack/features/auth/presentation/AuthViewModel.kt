package me.egil_accamacho.classtrack.features.auth.presentation

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
import me.egil_accamacho.classtrack.features.auth.domain.model.UserRole
import me.egil_accamacho.classtrack.features.auth.domain.usecase.LoginUseCase
import me.egil_accamacho.classtrack.features.auth.domain.usecase.LogoutUseCase
import me.egil_accamacho.classtrack.features.auth.domain.usecase.RegisterUseCase
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    private val _actions = MutableSharedFlow<AuthAction>()
    val actions: SharedFlow<AuthAction> = _actions.asSharedFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.EmailChanged           -> _state.update { it.copy(email = event.value, fieldErrors = it.fieldErrors - AuthField.EMAIL, generalError = null) }
            is AuthEvent.PasswordChanged        -> _state.update { it.copy(password = event.value, fieldErrors = it.fieldErrors - AuthField.PASSWORD, generalError = null) }
            is AuthEvent.FullNameChanged        -> _state.update { it.copy(fullName = event.value, fieldErrors = it.fieldErrors - AuthField.FULL_NAME, generalError = null) }
            is AuthEvent.ConfirmPasswordChanged -> _state.update { it.copy(confirmPassword = event.value, fieldErrors = it.fieldErrors - AuthField.CONFIRM_PASSWORD, generalError = null) }
            is AuthEvent.RoleChanged            -> _state.update { it.copy(role = event.value) }
            AuthEvent.SubmitLogin               -> submitLogin()
            AuthEvent.SubmitRegister            -> submitRegister()
            AuthEvent.NavigateToRegister        -> emit(AuthAction.NavigateToRegister)
            AuthEvent.NavigateToLogin           -> emit(AuthAction.NavigateToLogin)
            AuthEvent.DismissError              -> _state.update { it.copy(generalError = null) }
        }
    }

    // ── Login ─────────────────────────────────────────────────────────────────

    private fun submitLogin() {
        val s = _state.value
        val errors = buildMap<AuthField, String> {
            if (s.email.isBlank()) put(AuthField.EMAIL, "El correo es requerido")
            else if (!s.email.contains('@')) put(AuthField.EMAIL, "Correo inválido")
            if (s.password.isBlank()) put(AuthField.PASSWORD, "La contraseña es requerida")
        }
        if (errors.isNotEmpty()) { _state.update { it.copy(fieldErrors = errors) }; return }

        viewModelScope.launch {
            _state.update { it.copy(loading = true, generalError = null) }
            when (val result = loginUseCase(s.email.trim(), s.password)) {
                is Resource.Success -> {
                    _state.update { it.copy(loading = false) }
                    emit(if (result.data == UserRole.TEACHER) AuthAction.NavigateToHomeTeacher else AuthAction.NavigateToHomeStudent)
                }
                is Resource.Error -> {
                    _state.update { it.copy(loading = false, generalError = result.error.message) }
                }
                Resource.Loading -> Unit
            }
        }
    }

    // ── Register ──────────────────────────────────────────────────────────────

    private fun submitRegister() {
        val s = _state.value
        val errors = buildMap<AuthField, String> {
            if (s.fullName.isBlank()) put(AuthField.FULL_NAME, "El nombre es requerido")
            if (s.email.isBlank()) put(AuthField.EMAIL, "El correo es requerido")
            else if (!s.email.contains('@')) put(AuthField.EMAIL, "Correo inválido")
            if (s.password.length < 8) put(AuthField.PASSWORD, "Mínimo 8 caracteres")
            if (s.confirmPassword != s.password) put(AuthField.CONFIRM_PASSWORD, "Las contraseñas no coinciden")
        }
        if (errors.isNotEmpty()) { _state.update { it.copy(fieldErrors = errors) }; return }

        viewModelScope.launch {
            _state.update { it.copy(loading = true, generalError = null) }
            when (val result = registerUseCase(s.fullName.trim(), s.email.trim(), s.password, s.role)) {
                is Resource.Success -> {
                    _state.update { it.copy(loading = false) }
                    emit(if (result.data == UserRole.TEACHER) AuthAction.NavigateToHomeTeacher else AuthAction.NavigateToHomeStudent)
                }
                is Resource.Error -> {
                    _state.update { it.copy(loading = false, generalError = result.error.message) }
                }
                Resource.Loading -> Unit
            }
        }
    }

    private fun emit(action: AuthAction) {
        viewModelScope.launch { _actions.emit(action) }
    }
}
