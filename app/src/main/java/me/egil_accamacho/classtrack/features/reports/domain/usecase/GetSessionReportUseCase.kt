package me.egil_accamacho.classtrack.features.reports.domain.usecase

import me.egil_accamacho.classtrack.features.reports.domain.repository.ReportsRepository
import javax.inject.Inject

class GetSessionReportUseCase @Inject constructor(
    private val repository: ReportsRepository,
) {
    suspend operator fun invoke(sessionId: Long) = repository.getSessionReport(sessionId)
}
