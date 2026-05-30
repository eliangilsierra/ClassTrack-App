package me.egil_accamacho.classtrack.features.attendance.data.repository

import me.egil_accamacho.classtrack.core.common.Resource
import me.egil_accamacho.classtrack.core.common.safeCall
import me.egil_accamacho.classtrack.core.network.ErrorMapper
import me.egil_accamacho.classtrack.features.attendance.data.remote.AttendanceApi
import me.egil_accamacho.classtrack.features.attendance.data.remote.dto.CheckInRequest
import me.egil_accamacho.classtrack.features.attendance.data.remote.dto.CreateSessionRequest
import me.egil_accamacho.classtrack.features.attendance.domain.model.AttendanceRecord
import me.egil_accamacho.classtrack.features.attendance.domain.model.AttendanceSession
import me.egil_accamacho.classtrack.features.attendance.domain.model.CheckInResult
import me.egil_accamacho.classtrack.features.attendance.domain.repository.AttendanceRepository
import javax.inject.Inject

class AttendanceRepositoryImpl @Inject constructor(
    private val api: AttendanceApi,
    private val errorMapper: ErrorMapper,
) : AttendanceRepository {

    override suspend fun createSession(courseId: Long): Resource<AttendanceSession> =
        safeCall(errorMapper) {
            val dto = api.createSession(CreateSessionRequest(courseId))
            AttendanceSession(
                sessionId = dto.sessionId,
                courseId  = dto.courseId,
                qrToken   = dto.qrToken,
                expiresAt = dto.expiresAt,
            )
        }

    override suspend fun closeSession(sessionId: Long): Resource<Unit> =
        safeCall(errorMapper) { api.closeSession(sessionId) }

    override suspend fun checkIn(sessionId: Long, latitude: Double, longitude: Double): Resource<CheckInResult> =
        safeCall(errorMapper) {
            val dto = api.checkIn(CheckInRequest(sessionId, latitude, longitude))
            CheckInResult(attendanceId = dto.attendanceId, registeredAt = dto.registeredAt)
        }

    override suspend fun getRecords(sessionId: Long): Resource<List<AttendanceRecord>> =
        safeCall(errorMapper) {
            api.getRecords(sessionId).map {
                AttendanceRecord(studentId = it.studentId, studentName = it.studentName, registeredAt = it.registeredAt)
            }
        }
}
