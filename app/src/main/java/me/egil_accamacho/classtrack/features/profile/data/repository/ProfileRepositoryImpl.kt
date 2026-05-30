package me.egil_accamacho.classtrack.features.profile.data.repository

import me.egil_accamacho.classtrack.core.common.Resource
import me.egil_accamacho.classtrack.core.common.safeCall
import me.egil_accamacho.classtrack.core.network.ErrorMapper
import me.egil_accamacho.classtrack.features.profile.data.remote.ProfileApi
import me.egil_accamacho.classtrack.features.profile.domain.model.DigitalId
import me.egil_accamacho.classtrack.features.profile.domain.model.UserProfile
import me.egil_accamacho.classtrack.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi,
    private val errorMapper: ErrorMapper,
) : ProfileRepository {

    override suspend fun getProfile(): Resource<UserProfile> = safeCall(errorMapper) {
        val dto = api.getProfile()
        UserProfile(
            id = dto.id,
            fullName = dto.fullName,
            email = dto.email,
            role = dto.role,
        )
    }

    override suspend fun getDigitalId(): Resource<DigitalId> = safeCall(errorMapper) {
        val dto = api.getDigitalId()
        DigitalId(
            userId = dto.userId,
            fullName = dto.fullName,
            studentCode = dto.studentCode,
            qrContent = dto.qrContent,
        )
    }
}
