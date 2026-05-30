package me.egil_accamacho.classtrack.features.auth.presentation

import me.egil_accamacho.classtrack.features.auth.domain.model.UserRole

// ── UiState ───────────────────────────────────────────────────────────────────

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val fullName: String = "",
    val confirmPassword: String = "",
    val role: UserRole = UserRole.STUDENT,
    val loading: Boolean = false,
    val generalError: String? = null,
    val fieldErrors: Map<AuthField, String> = emptyMap(),
)

enum class AuthField {
    FULL_NAME, EMAIL, PASSWORD, CONFIRM_PASSWORD
}

// ── UiEvent (user actions / system triggers) ──────────────────────────────────

sealed interface AuthEvent {
    data class EmailChanged(val value: String) : AuthEvent
    data class PasswordChanged(val value: String) : AuthEvent
    data class FullNameChanged(val value: String) : AuthEvent
    data class ConfirmPasswordChanged(val value: String) : AuthEvent
    data class RoleChanged(val value: UserRole) : AuthEvent
    data object SubmitLogin : AuthEvent
    data object SubmitRegister : AuthEvent
    data object NavigateToRegister : AuthEvent
    data object NavigateToLogin : AuthEvent
    data object DismissError : AuthEvent
}

// ── UiAction (one-shot side-effects: navigate, snackbar) ─────────────────────

sealed interface AuthAction {
    data object NavigateToLogin : AuthAction
    data object NavigateToRegister : AuthAction
    data object NavigateToHomeTeacher : AuthAction
    data object NavigateToHomeStudent : AuthAction
}
