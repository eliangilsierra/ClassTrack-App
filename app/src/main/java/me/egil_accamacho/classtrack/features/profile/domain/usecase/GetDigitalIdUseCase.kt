package me.egil_accamacho.classtrack.features.profile.domain.usecase

import me.egil_accamacho.classtrack.core.common.Resource
import me.egil_accamacho.classtrack.features.profile.domain.model.DigitalId
import me.egil_accamacho.classtrack.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetDigitalIdUseCase @Inject constructor(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(): Resource<DigitalId> = repository.getDigitalId()
}
