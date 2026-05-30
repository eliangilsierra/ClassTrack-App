package me.egil_accamacho.classtrack.features.reports.data.remote

import me.egil_accamacho.classtrack.core.network.ApiEnvelope
import me.egil_accamacho.classtrack.features.reports.data.remote.dto.CourseReportResponse
import me.egil_accamacho.classtrack.features.reports.data.remote.dto.SessionReportResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ReportsApi {

    @GET("reports/sessions/{sessionId}")
    suspend fun getSessionReport(@Path("sessionId") sessionId: Long): ApiEnvelope<SessionReportResponse>

    @GET("reports/courses/{courseId}")
    suspend fun getCourseReport(@Path("courseId") courseId: Long): ApiEnvelope<CourseReportResponse>
}
