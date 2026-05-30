package me.egil_accamacho.classtrack.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.SharedFlow
import me.egil_accamacho.classtrack.core.session.SessionState
import me.egil_accamacho.classtrack.features.auth.presentation.LoginScreen
import me.egil_accamacho.classtrack.features.auth.presentation.RegisterScreen
import me.egil_accamacho.classtrack.features.auth.presentation.SplashScreen
import me.egil_accamacho.classtrack.features.courses.presentation.CourseDetailScreen
import me.egil_accamacho.classtrack.features.courses.presentation.CoursesScreen
import me.egil_accamacho.classtrack.features.courses.presentation.CreateCourseScreen
import me.egil_accamacho.classtrack.features.home.presentation.HomeStudentScreen
import me.egil_accamacho.classtrack.features.home.presentation.HomeTeacherScreen
import me.egil_accamacho.classtrack.features.attendance.presentation.AttendanceQrScreen
import me.egil_accamacho.classtrack.features.reports.presentation.AttendanceReportScreen
import me.egil_accamacho.classtrack.features.reports.presentation.CourseReportScreen
import me.egil_accamacho.classtrack.features.attendance.presentation.AttendanceScannerScreen
import me.egil_accamacho.classtrack.features.attendance.presentation.AttendanceSuccessScreen
import me.egil_accamacho.classtrack.features.attendance.presentation.CreateAttendanceSessionScreen
import me.egil_accamacho.classtrack.features.profile.presentation.DigitalIdScreen
import me.egil_accamacho.classtrack.features.profile.presentation.ProfileScreen
import me.egil_accamacho.classtrack.features.students.presentation.LinkStudentsScreen
import me.egil_accamacho.classtrack.features.students.presentation.ScanStudentQrScreen
import me.egil_accamacho.classtrack.features.students.presentation.StudentLinkedSuccessScreen

/**
 * Root composable for the entire app navigation graph.
 *
 * Always starts at [Destination.Splash]. The Splash placeholder reads
 * [RootViewModel.postSplashDestination] and navigates to Login or Home once
 * the DataStore session check completes.
 *
 * Phase 3 — all non-Splash composables are placeholders.
 * Phases 4–9 will replace each placeholder with the real screen.
 *
 * @param sessionState Current auth state — drives role-aware bottom navigation tabs.
 */
@Composable
fun ClassTrackNavGraph(
    sessionState: SessionState = SessionState.Unauthenticated,
    navigationEvent: SharedFlow<Destination>? = null,
) {
    val navController = rememberNavController()
    val isTeacher = (sessionState as? SessionState.Authenticated)?.isTeacher ?: false

    LaunchedEffect(navigationEvent) {
        navigationEvent?.collect { destination ->
            navController.navigate(destination) {
                popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
            }
        }
    }

    CtBottomNavScaffold(
        navController = navController,
        isTeacher = isTeacher,
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Splash,
            modifier = Modifier.padding(padding),
        ) {

            // ── Auth ──────────────────────────────────────────────────────────
            composable<Destination.Splash> {
                SplashScreen(navController = navController)
            }
            composable<Destination.Login> {
                LoginScreen(navController = navController)
            }
            composable<Destination.Register> {
                RegisterScreen(navController = navController)
            }

            // ── Home ──────────────────────────────────────────────────────────
            composable<Destination.HomeTeacher> {
                HomeTeacherScreen(navController = navController)
            }
            composable<Destination.HomeStudent> {
                HomeStudentScreen(navController = navController)
            }

            // ── Courses ───────────────────────────────────────────────────────
            composable<Destination.Courses> {
                CoursesScreen(navController = navController)
            }
            composable<Destination.CreateCourse> {
                CreateCourseScreen(navController = navController)
            }
            composable<Destination.CourseDetail> {
                CourseDetailScreen(navController = navController)
            }

            // ── Student Linking ───────────────────────────────────────────────
            composable<Destination.LinkStudents> {
                LinkStudentsScreen(navController = navController)
            }
            composable<Destination.ScanStudentQr> {
                ScanStudentQrScreen(navController = navController)
            }
            composable<Destination.StudentLinkedSuccess> {
                StudentLinkedSuccessScreen(navController = navController)
            }

            // ── Attendance ────────────────────────────────────────────────────
            composable<Destination.CreateAttendanceSession> {
                CreateAttendanceSessionScreen(navController = navController)
            }
            composable<Destination.AttendanceQr> {
                AttendanceQrScreen(navController = navController)
            }
            composable<Destination.AttendanceScanner> {
                AttendanceScannerScreen(navController = navController)
            }
            composable<Destination.AttendanceSuccess> {
                AttendanceSuccessScreen(navController = navController)
            }
            composable<Destination.AttendanceReport> {
                AttendanceReportScreen(navController = navController)
            }
            composable<Destination.CourseReport> {
                CourseReportScreen(navController = navController)
            }

            // ── Profile ───────────────────────────────────────────────────────
            composable<Destination.Profile> {
                ProfileScreen(navController = navController)
            }
            composable<Destination.DigitalId> {
                DigitalIdScreen(navController = navController)
            }
            composable<Destination.LogoutConfirm> {
                PlaceholderScreen("Logout Confirm") // handled inline in ProfileScreen dialog
            }
        }
    }
}

// ── Private composables ───────────────────────────────────────────────────────

/** Generic placeholder used for screens not yet implemented (Phase 5–9). */
@Composable
private fun PlaceholderScreen(name: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "TODO: $name",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
