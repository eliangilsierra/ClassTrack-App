package me.egil_accamacho.classtrack.features.profile.data.remote

import me.egil_accamacho.classtrack.features.profile.data.remote.dto.DigitalIdResponse
import me.egil_accamacho.classtrack.features.profile.data.remote.dto.ProfileResponse
import retrofit2.http.GET

interface ProfileApi {

    @GET("profile")
    suspend fun getProfile(): ProfileResponse

    @GET("profile/digital-id")
    suspend fun getDigitalId(): DigitalIdResponse
}
