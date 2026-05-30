package me.egil_accamacho.classtrack.features.auth.domain.repository

import me.egil_accamacho.classtrack.core.common.Resource
import me.egil_accamacho.classtrack.features.auth.domain.model.UserRole

interface AuthRepository {

    suspend fun login(email: String, password: String): Resource<UserRole>

    suspend fun register(
        fullName: String,
        email: String,
        password: String,
        role: UserRole,
    ): Resource<UserRole>

    suspend fun logout()
}
