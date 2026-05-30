package me.egil_accamacho.classtrack.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.egil_accamacho.classtrack.core.session.SessionManager
import me.egil_accamacho.classtrack.core.session.SessionState
import javax.inject.Inject

/**
 * Single ViewModel owned by MainActivity for app-level concerns:
 *   1. Determines the post-splash destination from DataStore on cold start.
 *   2. Exposes live session state for role-aware bottom navigation.
 */
@HiltViewModel
class RootViewModel @Inject constructor(
    private val sessionManager: SessionManager,
) : ViewModel() {

    /** Live session state — used by CtBottomNavScaffold to switch teacher/student tabs. */
    val sessionState: StateFlow<SessionState> = sessionManager.sessionState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = SessionState.Unauthenticated,
        )

    /**
     * Destination the Splash screen should navigate to once the first DataStore read
     * resolves. Null while the read is in progress (Splash shows a spinner during this).
     */
    private val _postSplashDestination = MutableStateFlow<Destination?>(null)
    val postSplashDestination: StateFlow<Destination?> = _postSplashDestination.asStateFlow()

    /** Emits [Destination.Login] when the server returns 401 (token expired/revoked). */
    private val _navigationEvent = MutableSharedFlow<Destination>(extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<Destination> = _navigationEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            val state = sessionManager.sessionState.first()
            _postSplashDestination.value = when (state) {
                is SessionState.Authenticated -> {
                    if (state.isTeacher) Destination.HomeTeacher else Destination.HomeStudent
                }
                SessionState.Unauthenticated -> Destination.Login
            }
        }

        viewModelScope.launch {
            sessionManager.sessionExpiredEvent.collect {
                _navigationEvent.emit(Destination.Login)
            }
        }
    }
}
