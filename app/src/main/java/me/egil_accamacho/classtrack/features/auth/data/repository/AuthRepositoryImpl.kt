package me.egil_accamacho.classtrack.features.auth.data.repository

import me.egil_accamacho.classtrack.core.common.Resource
import me.egil_accamacho.classtrack.core.common.safeCall
import me.egil_accamacho.classtrack.core.network.ErrorMapper
import me.egil_accamacho.classtrack.core.session.SessionManager
import me.egil_accamacho.classtrack.features.auth.data.remote.AuthApi
import me.egil_accamacho.classtrack.features.auth.data.remote.dto.LoginRequest
import me.egil_accamacho.classtrack.features.auth.data.remote.dto.RegisterRequest
import me.egil_accamacho.classtrack.features.auth.domain.model.UserRole
import me.egil_accamacho.classtrack.features.auth.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val sessionManager: SessionManager,
    private val errorMapper: ErrorMapper,
) : AuthRepository {

    override suspend fun login(email: String, password: String): Resource<UserRole> =
        safeCall(errorMapper) {
            val envelope = api.login(LoginRequest(email, password))
            val data = envelope.data ?: throw IllegalStateException("Respuesta vacía del servidor")
            val role = parseRole(data.role)
            sessionManager.save(jwt = data.token, userId = data.userId, role = data.role)
            role
        }

    override suspend fun register(
        fullName: String,
        email: String,
        password: String,
        role: UserRole,
    ): Resource<UserRole> = safeCall(errorMapper) {
        val envelope = api.register(
            RegisterRequest(
                fullName = fullName,
                email = email,
                password = password,
                role = role.name,
            )
        )
        val data = envelope.data ?: throw IllegalStateException("Respuesta vacía del servidor")
        // Register response only returns userId + token, role is known from the request
        sessionManager.save(jwt = data.token, userId = data.userId, role = role.name)
        role
    }

    override suspend fun logout() {
        sessionManager.clear()
    }

    private fun parseRole(raw: String): UserRole = when (raw.uppercase()) {
        "TEACHER" -> UserRole.TEACHER
        "STUDENT" -> UserRole.STUDENT
        else -> throw IllegalStateException("Rol desconocido: $raw")
    }
}
