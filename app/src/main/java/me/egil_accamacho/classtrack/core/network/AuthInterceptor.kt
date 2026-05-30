package me.egil_accamacho.classtrack.core.network

import me.egil_accamacho.classtrack.core.session.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * OkHttp interceptor that attaches `Authorization: Bearer <jwt>` to every
 * request except those that target public auth endpoints (`/auth/`).
 *
 * The JWT is read from [SessionManager.currentJwt] which is an in-memory
 * volatile field — no coroutine / blocking needed inside the interceptor chain.
 */
@Singleton
class AuthInterceptor @Inject constructor(
    private val sessionManager: SessionManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        // Skip auth header for public endpoints
        val isPublicEndpoint = original.url.encodedPath.contains("/auth/")
        if (isPublicEndpoint) return chain.proceed(original)

        val jwt = sessionManager.currentJwt
        val request = if (jwt != null) {
            original.newBuilder()
                .header("Authorization", "Bearer $jwt")
                .build()
        } else {
            original
        }

        val response = chain.proceed(request)

        if (response.code == 401) {
            sessionManager.signalSessionExpired()
        }

        return response
    }
}
