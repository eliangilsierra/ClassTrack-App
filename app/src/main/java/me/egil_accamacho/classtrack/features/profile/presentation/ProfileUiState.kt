package me.egil_accamacho.classtrack.features.profile.presentation

import android.graphics.Bitmap
import me.egil_accamacho.classtrack.features.profile.domain.model.DigitalId
import me.egil_accamacho.classtrack.features.profile.domain.model.UserProfile

// ── UiState ───────────────────────────────────────────────────────────────────

data class ProfileUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val profile: UserProfile? = null,
    val digitalId: DigitalId? = null,
    val qrBitmap: Bitmap? = null,
    val showLogoutDialog: Boolean = false,
)

// ── UiEvent ───────────────────────────────────────────────────────────────────

sealed interface ProfileEvent {
    data object LoadDigitalId : ProfileEvent
    data object ShowLogoutDialog : ProfileEvent
    data object DismissLogoutDialog : ProfileEvent
    data object ConfirmLogout : ProfileEvent
    data object DismissError : ProfileEvent
    data object NavigateToDigitalId : ProfileEvent
}

// ── UiAction (one-shot side-effects) ─────────────────────────────────────────

sealed interface ProfileAction {
    data object NavigateToDigitalId : ProfileAction
    data object NavigateToLogin : ProfileAction
}
