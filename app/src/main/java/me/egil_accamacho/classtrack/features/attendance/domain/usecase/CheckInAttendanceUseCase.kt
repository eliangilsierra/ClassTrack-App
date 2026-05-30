package me.egil_accamacho.classtrack.features.attendance.domain.usecase

import me.egil_accamacho.classtrack.features.attendance.domain.repository.AttendanceRepository
import javax.inject.Inject

class CheckInAttendanceUseCase @Inject constructor(
    private val repository: AttendanceRepository,
) {
    suspend operator fun invoke(sessionId: Long, latitude: Double, longitude: Double) =
        repository.checkIn(sessionId, latitude, longitude)
}
