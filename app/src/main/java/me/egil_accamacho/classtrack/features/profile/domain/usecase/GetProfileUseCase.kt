package me.egil_accamacho.classtrack.features.profile.domain.usecase

import me.egil_accamacho.classtrack.core.common.Resource
import me.egil_accamacho.classtrack.features.profile.domain.model.UserProfile
import me.egil_accamacho.classtrack.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(): Resource<UserProfile> = repository.getProfile()
}
