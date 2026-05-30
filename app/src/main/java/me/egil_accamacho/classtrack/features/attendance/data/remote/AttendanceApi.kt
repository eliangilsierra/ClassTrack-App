package me.egil_accamacho.classtrack.features.attendance.data.remote

import me.egil_accamacho.classtrack.core.network.ApiEnvelope
import me.egil_accamacho.classtrack.features.attendance.data.remote.dto.AttendanceRecordResponse
import me.egil_accamacho.classtrack.features.attendance.data.remote.dto.AttendanceSessionResponse
import me.egil_accamacho.classtrack.features.attendance.data.remote.dto.CheckInRequest
import me.egil_accamacho.classtrack.features.attendance.data.remote.dto.CheckInResponse
import me.egil_accamacho.classtrack.features.attendance.data.remote.dto.CreateSessionRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AttendanceApi {

    @POST("attendance/sessions")
    suspend fun createSession(@Body body: CreateSessionRequest): ApiEnvelope<AttendanceSessionResponse>

    @POST("attendance/sessions/{sessionId}/close")
    suspend fun closeSession(@Path("sessionId") sessionId: Long): ApiEnvelope<Unit?>

    @POST("attendance/checkin")
    suspend fun checkIn(@Body body: CheckInRequest): ApiEnvelope<CheckInResponse>

    @GET("attendance/sessions/{sessionId}/records")
    suspend fun getRecords(@Path("sessionId") sessionId: Long): ApiEnvelope<List<AttendanceRecordResponse>>
}
