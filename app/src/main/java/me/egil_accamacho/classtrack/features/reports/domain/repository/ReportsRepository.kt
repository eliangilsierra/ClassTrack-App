package me.egil_accamacho.classtrack.features.reports.domain.repository

import me.egil_accamacho.classtrack.core.common.Resource
import me.egil_accamacho.classtrack.features.reports.domain.model.CourseReport
import me.egil_accamacho.classtrack.features.reports.domain.model.SessionReport

interface ReportsRepository {
    suspend fun getSessionReport(sessionId: Long): Resource<SessionReport>
    suspend fun getCourseReport(courseId: Long): Resource<CourseReport>
}
