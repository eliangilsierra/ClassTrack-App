package me.egil_accamacho.classtrack.core.session

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

// ── DataStore preference keys ─────────────────────────────────────────────────
private object PrefsKeys {
    val JWT     = stringPreferencesKey("jwt")
    val USER_ID = longPreferencesKey("user_id")
    val ROLE    = stringPreferencesKey("role")
}

// ── Contract ──────────────────────────────────────────────────────────────────

interface SessionManager {
    /** Hot flow that emits the current [SessionState] whenever it changes. */
    val sessionState: Flow<SessionState>

    /** One-shot events emitted when the server returns 401 (token expired/revoked). */
    val sessionExpiredEvent: SharedFlow<Unit>

    /**
     * In-memory JWT cache for synchronous access inside OkHttp interceptors.
     * Updated immediately on [save] and cleared on [clear].
     */
    val currentJwt: String?

    /** Persist the session after a successful login/register. */
    suspend fun save(jwt: String, userId: Long, role: String)

    /** Erase the session on logout or JWT expiry. */
    suspend fun clear()

    /**
     * Non-suspend entry point for OkHttp interceptors (background threads).
     * Emits [sessionExpiredEvent] and clears the DataStore session.
     */
    fun signalSessionExpired()
}

// ── Implementation ────────────────────────────────────────────────────────────

@Singleton
class DataStoreSessionManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SessionManager {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _sessionExpiredEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    override val sessionExpiredEvent: SharedFlow<Unit> = _sessionExpiredEvent.asSharedFlow()

    /**
     * In-memory cache — keeps the JWT available synchronously for [AuthInterceptor]
     * without blocking the OkHttp dispatcher thread.
     */
    @Volatile override var currentJwt: String? = null
        private set

    override val sessionState: Flow<SessionState> = dataStore.data
        .catch { e ->
            Log.e("SessionManager", "Error reading DataStore: ${e.message}", e)
            // If DataStore is corrupted, treat as unauthenticated
            emit(androidx.datastore.preferences.core.emptyPreferences())
        }
        .map { prefs ->
            val jwt    = prefs[PrefsKeys.JWT]
            val userId = prefs[PrefsKeys.USER_ID]
            val role   = prefs[PrefsKeys.ROLE]

            if (jwt != null && userId != null && role != null) {
                currentJwt = jwt          // keep in-memory cache in sync
                SessionState.Authenticated(jwt = jwt, userId = userId, role = role)
            } else {
                currentJwt = null
                SessionState.Unauthenticated
            }
        }

    override suspend fun save(jwt: String, userId: Long, role: String) {
        currentJwt = jwt                 // update cache immediately
        dataStore.edit { prefs ->
            prefs[PrefsKeys.JWT]     = jwt
            prefs[PrefsKeys.USER_ID] = userId
            prefs[PrefsKeys.ROLE]    = role
        }
    }

    override suspend fun clear() {
        currentJwt = null               // clear cache immediately
        dataStore.edit { it.clear() }
    }

    override fun signalSessionExpired() {
        _sessionExpiredEvent.tryEmit(Unit)
        scope.launch { clear() }
    }
}
