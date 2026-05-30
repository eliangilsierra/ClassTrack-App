package me.egil_accamacho.classtrack.features.auth.domain.usecase

import me.egil_accamacho.classtrack.core.common.Resource
import me.egil_accamacho.classtrack.features.auth.domain.model.UserRole
import me.egil_accamacho.classtrack.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(
        fullName: String,
        email: String,
        password: String,
        role: UserRole,
    ): Resource<UserRole> = repository.register(fullName, email, password, role)
}
