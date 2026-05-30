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
        val envelope = api.getProfile()
        val data = envelope.data ?: throw Exception("Respuesta de perfil vacía")
        UserProfile(
            id = data.id,
            fullName = data.fullName,
            email = data.email,
            role = data.role,
        )
    }

    override suspend fun getDigitalId(): Resource<DigitalId> = safeCall(errorMapper) {
        val envelope = api.getDigitalId()
        val data = envelope.data ?: throw Exception("Respuesta de ID digital vacía")
        DigitalId(
            userId = data.userId,
            fullName = data.fullName,
            studentCode = data.studentCode,
            qrContent = data.qrContent,
        )
    }
}
