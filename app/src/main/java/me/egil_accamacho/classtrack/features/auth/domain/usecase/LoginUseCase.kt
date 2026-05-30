package me.egil_accamacho.classtrack.features.auth.domain.usecase

import me.egil_accamacho.classtrack.core.common.Resource
import me.egil_accamacho.classtrack.features.auth.domain.model.UserRole
import me.egil_accamacho.classtrack.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String): Resource<UserRole> =
        repository.login(email, password)
}
