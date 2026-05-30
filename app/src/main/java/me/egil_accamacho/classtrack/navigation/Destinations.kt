package me.egil_accamacho.classtrack.navigation

import kotlinx.serialization.Serializable

/**
 * Type-safe navigation routes for the entire app.
 * Used with Navigation Compose 2.8+ type-safe API.
 *
 * 19 destinations:
 *   Splash, Login, Register
 *   HomeTeacher, HomeStudent
 *   Courses, CreateCourse, CourseDetail
 *   LinkStudents, ScanStudentQr, StudentLinkedSuccess
 *   CreateAttendanceSession, AttendanceQr, AttendanceScanner, AttendanceSuccess, AttendanceReport
 *   Profile, DigitalId, LogoutConfirm
 */
@Serializable
sealed class Destination {

    // ── Auth ──────────────────────────────────────────────────────────────────
    @Serializable data object Splash : Destination()
    @Serializable data object Login : Destination()
    @Serializable data object Register : Destination()

    // ── Home ──────────────────────────────────────────────────────────────────
    @Serializable data object HomeTeacher : Destination()
    @Serializable data object HomeStudent : Destination()

    // ── Courses ───────────────────────────────────────────────────────────────
    @Serializable data object Courses : Destination()
    @Serializable data object CreateCourse : Destination()
    @Serializable data class CourseDetail(val courseId: Long) : Destination()

    // ── Student Linking ───────────────────────────────────────────────────────
    @Serializable data class LinkStudents(val courseId: Long) : Destination()
    @Serializable data class ScanStudentQr(val courseId: Long) : Destination()
    @Serializable data class StudentLinkedSuccess(
        val studentId: Long,
        val courseId: Long,
    ) : Destination()

    // ── Attendance ────────────────────────────────────────────────────────────
    @Serializable data class CreateAttendanceSession(val courseId: Long) : Destination()
    @Serializable data class AttendanceQr(
        val sessionId: Long,
        val qrToken: String,
        val expiresAt: String,
    ) : Destination()
    @Serializable data object AttendanceScanner : Destination()
    @Serializable data class AttendanceSuccess(
        val sessionId: Long,
        val registeredAt: String,
    ) : Destination()
    @Serializable data class AttendanceReport(val sessionId: Long) : Destination()
    @Serializable data class CourseReport(val courseId: Long) : Destination()

    // ── Profile ───────────────────────────────────────────────────────────────
    @Serializable data object Profile : Destination()
    @Serializable data object DigitalId : Destination()
    @Serializable data object LogoutConfirm : Destination()
}
