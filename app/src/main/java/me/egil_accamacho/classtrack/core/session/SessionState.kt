package me.egil_accamacho.classtrack.core.session

/**
 * Represents the authentication state of the current user.
 * Emitted by [SessionManager.sessionState].
 */
sealed interface SessionState {

    /** No valid JWT in DataStore — user must log in. */
    data object Unauthenticated : SessionState

    /**
     * A valid JWT is present and the user identity is known.
     *
     * @param jwt   Bearer token for all protected API calls.
     * @param userId Numeric user identifier (from login response).
     * @param role  "TEACHER" or "STUDENT" (from login response).
     */
    data class Authenticated(
        val jwt: String,
        val userId: Long,
        val role: String
    ) : SessionState {
        val isTeacher: Boolean get() = role == "TEACHER"
        val isStudent: Boolean get() = role == "STUDENT"
    }
}
