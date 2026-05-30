package me.egil_accamacho.classtrack.features.attendance.domain.usecase

import me.egil_accamacho.classtrack.features.attendance.domain.repository.AttendanceRepository
import javax.inject.Inject

class CreateAttendanceSessionUseCase @Inject constructor(
    private val repository: AttendanceRepository,
) {
    suspend operator fun invoke(courseId: Long, durationMinutes: Int? = null) =
        repository.createSession(courseId, durationMinutes)
}
