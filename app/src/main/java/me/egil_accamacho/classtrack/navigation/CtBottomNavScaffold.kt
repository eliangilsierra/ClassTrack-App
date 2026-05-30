package me.egil_accamacho.classtrack.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import me.egil_accamacho.classtrack.ui.components.CtBottomNavigation
import me.egil_accamacho.classtrack.ui.components.studentNavItems
import me.egil_accamacho.classtrack.ui.components.teacherNavItems

/**
 * Single-screen Scaffold that shows [CtBottomNavigation] only on top-level destinations.
 *
 * Top-level screens (bottom nav visible):
 *   Teacher: HomeTeacher, Courses, Profile
 *   Student: HomeStudent, Profile
 *
 * All other destinations (forms, scanners, success screens, etc.) hide the bottom bar.
 *
 * @param navController NavHostController shared with the NavHost inside.
 * @param isTeacher     True when the authenticated user is a TEACHER; drives tab count.
 * @param content       The NavHost or any inner content; receives scaffold inner padding.
 */
@Composable
fun CtBottomNavScaffold(
    navController: NavHostController,
    isTeacher: Boolean,
    content: @Composable (PaddingValues) -> Unit,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDest = backStackEntry?.destination

    val showBottomNav = currentDest?.isTopLevel(isTeacher) == true
    val currentRoute  = currentDest?.toBottomNavRouteString() ?: ""

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                CtBottomNavigation(
                    items = if (isTeacher) teacherNavItems else studentNavItems,
                    currentRoute = currentRoute,
                    onItemClick = { route ->
                        navController.navigateToTopLevel(route, isTeacher)
                    },
                )
            }
        },
    ) { padding ->
        content(padding)
    }
}

// ── Private helpers ───────────────────────────────────────────────────────────

private fun NavDestination.isTopLevel(isTeacher: Boolean): Boolean = if (isTeacher) {
    hasRoute<Destination.HomeTeacher>() ||
    hasRoute<Destination.Courses>() ||
    hasRoute<Destination.Profile>()
} else {
    hasRoute<Destination.HomeStudent>() ||
    hasRoute<Destination.Profile>()
}

private fun NavDestination.toBottomNavRouteString(): String = when {
    hasRoute<Destination.HomeTeacher>() -> "home_teacher"
    hasRoute<Destination.HomeStudent>() -> "home_student"
    hasRoute<Destination.Courses>()     -> "courses"
    hasRoute<Destination.Profile>()     -> "profile"
    else                                -> ""
}

private fun NavHostController.navigateToTopLevel(route: String, isTeacher: Boolean) {
    val destination: Destination = when (route) {
        "home_teacher" -> Destination.HomeTeacher
        "home_student" -> Destination.HomeStudent
        "courses"      -> Destination.Courses
        "profile"      -> Destination.Profile
        else           -> return
    }

    navigate(destination) {
        if (isTeacher) {
            popUpTo<Destination.HomeTeacher> { saveState = true }
        } else {
            popUpTo<Destination.HomeStudent> { saveState = true }
        }
        launchSingleTop = true
        restoreState = true
    }
}
