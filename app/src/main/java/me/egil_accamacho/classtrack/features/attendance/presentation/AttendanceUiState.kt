package me.egil_accamacho.classtrack.features.attendance.presentation

import android.graphics.Bitmap
import me.egil_accamacho.classtrack.features.attendance.domain.model.AttendanceRecord

// ── CreateAttendanceSession ───────────────────────────────────────────────────

data class CreateAttendanceSessionUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val courseName: String = "",
)

sealed interface CreateAttendanceSessionEvent {
    data object CreateSession : CreateAttendanceSessionEvent
    data object DismissError : CreateAttendanceSessionEvent
}

sealed interface CreateAttendanceSessionAction {
    data class NavigateToQr(
        val sessionId: Long,
        val qrToken: String,
        val expiresAt: String,
    ) : CreateAttendanceSessionAction
}

// ── AttendanceQr ──────────────────────────────────────────────────────────────

data class AttendanceQrUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val qrBitmap: Bitmap? = null,
    val remainingSeconds: Int = 0,
    val records: List<AttendanceRecord> = emptyList(),
)

sealed interface AttendanceQrEvent {
    data object CloseSession : AttendanceQrEvent
    data object DismissError : AttendanceQrEvent
}

sealed interface AttendanceQrAction {
    data class NavigateToReport(val sessionId: Long) : AttendanceQrAction
}

// ── AttendanceScanner ─────────────────────────────────────────────────────────

data class AttendanceScannerUiState(
    val processing: Boolean = false,
    val error: String? = null,
)

sealed interface AttendanceScannerEvent {
    data class QrDetected(val content: String) : AttendanceScannerEvent
    data object DismissError : AttendanceScannerEvent
}

sealed interface AttendanceScannerAction {
    data class NavigateToSuccess(val sessionId: Long, val registeredAt: String) : AttendanceScannerAction
}

// ── AttendanceSuccess ─────────────────────────────────────────────────────────

data class AttendanceSuccessUiState(
    val registeredAt: String = "",
)

sealed interface AttendanceSuccessEvent {
    data object GoHome : AttendanceSuccessEvent
}

sealed interface AttendanceSuccessAction {
    data object NavigateHome : AttendanceSuccessAction
}
