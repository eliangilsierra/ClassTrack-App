package me.egil_accamacho.classtrack.features.auth.data.remote

import me.egil_accamacho.classtrack.core.network.ApiEnvelope
import me.egil_accamacho.classtrack.features.auth.data.remote.dto.LoginRequest
import me.egil_accamacho.classtrack.features.auth.data.remote.dto.LoginResponse
import me.egil_accamacho.classtrack.features.auth.data.remote.dto.RegisterRequest
import me.egil_accamacho.classtrack.features.auth.data.remote.dto.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): ApiEnvelope<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): ApiEnvelope<RegisterResponse>
}
