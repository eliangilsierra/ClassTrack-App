package me.egil_accamacho.classtrack.features.attendance.domain.repository

import me.egil_accamacho.classtrack.core.common.Resource
import me.egil_accamacho.classtrack.features.attendance.domain.model.AttendanceRecord
import me.egil_accamacho.classtrack.features.attendance.domain.model.AttendanceSession
import me.egil_accamacho.classtrack.features.attendance.domain.model.CheckInResult

interface AttendanceRepository {
    suspend fun createSession(courseId: Long, durationMinutes: Int? = null): Resource<AttendanceSession>
    suspend fun closeSession(sessionId: Long): Resource<Unit>
    suspend fun checkIn(sessionId: Long, latitude: Double, longitude: Double): Resource<CheckInResult>
    suspend fun getRecords(sessionId: Long): Resource<List<AttendanceRecord>>
}
