package me.egil_accamacho.classtrack.features.profile.domain.repository

import me.egil_accamacho.classtrack.core.common.Resource
import me.egil_accamacho.classtrack.features.profile.domain.model.DigitalId
import me.egil_accamacho.classtrack.features.profile.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getProfile(): Resource<UserProfile>
    suspend fun getDigitalId(): Resource<DigitalId>
}
