package me.egil_accamacho.classtrack.features.reports.data.repository

import me.egil_accamacho.classtrack.core.common.Resource
import me.egil_accamacho.classtrack.core.common.safeCall
import me.egil_accamacho.classtrack.core.network.ErrorMapper
import me.egil_accamacho.classtrack.features.reports.data.remote.ReportsApi
import me.egil_accamacho.classtrack.features.reports.domain.model.CourseReport
import me.egil_accamacho.classtrack.features.reports.domain.model.SessionReport
import me.egil_accamacho.classtrack.features.reports.domain.repository.ReportsRepository
import javax.inject.Inject

class ReportsRepositoryImpl @Inject constructor(
    private val api: ReportsApi,
    private val errorMapper: ErrorMapper,
) : ReportsRepository {

    override suspend fun getSessionReport(sessionId: Long): Resource<SessionReport> =
        safeCall(errorMapper) {
            val dto = api.getSessionReport(sessionId).data
                ?: throw Exception("Reporte de sesión vacío")
            SessionReport(
                sessionId     = dto.sessionId,
                totalStudents = dto.totalStudents,
                attendees     = dto.attendees,
                absent        = dto.absent,
            )
        }

    override suspend fun getCourseReport(courseId: Long): Resource<CourseReport> =
        safeCall(errorMapper) {
            val dto = api.getCourseReport(courseId).data
                ?: throw Exception("Reporte de curso vacío")
            CourseReport(
                courseId       = dto.courseId,
                courseName     = dto.courseName,
                totalStudents  = dto.totalStudents,
                attendanceRate = dto.attendanceRate,
            )
        }
}
