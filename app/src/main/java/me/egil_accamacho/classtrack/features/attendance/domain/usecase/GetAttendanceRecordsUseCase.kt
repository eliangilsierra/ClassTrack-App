package me.egil_accamacho.classtrack.features.attendance.domain.usecase

import me.egil_accamacho.classtrack.features.attendance.domain.repository.AttendanceRepository
import javax.inject.Inject

class GetAttendanceRecordsUseCase @Inject constructor(
    private val repository: AttendanceRepository,
) {
    suspend operator fun invoke(sessionId: Long) = repository.getRecords(sessionId)
}
