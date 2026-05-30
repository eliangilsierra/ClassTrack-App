package me.egil_accamacho.classtrack.features.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import me.egil_accamacho.classtrack.navigation.Destination
import me.egil_accamacho.classtrack.ui.components.CourseCard
import me.egil_accamacho.classtrack.ui.components.CtTopAppBar
import me.egil_accamacho.classtrack.ui.components.EmptyState
import me.egil_accamacho.classtrack.ui.components.MetricCard
import me.egil_accamacho.classtrack.ui.theme.CtPrimary
import me.egil_accamacho.classtrack.ui.theme.CtSpacing

@Composable
fun HomeTeacherScreen(
    navController: NavController,
    viewModel: HomeTeacherViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                HomeTeacherAction.NavigateToCreateCourse      -> navController.navigate(Destination.CreateCourse)
                is HomeTeacherAction.NavigateToCourseDetail   -> navController.navigate(Destination.CourseDetail(action.courseId))
                HomeTeacherAction.NavigateToCourses           -> navController.navigate(Destination.Courses)
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(HomeTeacherEvent.DismissError)
        }
    }

    Scaffold(
        topBar = {
            val greeting = if (state.teacherName.isNotBlank())
                "Hola, Prof. ${state.teacherName.split(" ").firstOrNull() ?: ""}"
            else "Hola, Profesor"
            CtTopAppBar(title = greeting)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(HomeTeacherEvent.NavigateToCreateCourse) },
                containerColor = CtPrimary,
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Crear curso", tint = Color.White)
            }
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(CtSpacing.base),
            verticalArrangement = Arrangement.spacedBy(CtSpacing.base),
        ) {
            // ── Metric cards ─────────────────────────────────────────────────
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(CtSpacing.md),
                ) {
                    MetricCard(
                        label = "Cursos Activos",
                        value = "${state.courses.size}",
                        icon = Icons.Rounded.School,
                        modifier = Modifier.weight(1f),
                    )
                    MetricCard(
                        label = "Estudiantes",
                        value = "${state.courses.sumOf { it.studentCount }}",
                        icon = Icons.Rounded.Group,
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            // ── Section header ───────────────────────────────────────────────
            item {
                Spacer(modifier = Modifier.height(CtSpacing.sm))
                Text(
                    text = "Mis Cursos",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            // ── Courses list ─────────────────────────────────────────────────
            if (state.courses.isEmpty()) {
                item {
                    EmptyState(
                        title = "Sin cursos",
                        subtitle = "Crea tu primer curso usando el botón +",
                        icon = Icons.Rounded.School,
                    )
                }
            } else {
                items(state.courses.take(5), key = { it.id }) { course ->
                    CourseCard(
                        courseName = course.name,
                        studentCount = course.studentCount,
                        lastSession = "—",
                        onClick = { viewModel.onEvent(HomeTeacherEvent.NavigateToCourseDetail(course.id)) },
                    )
                }
                if (state.courses.size > 5) {
                    item {
                        Text(
                            text = "Ver todos los cursos →",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
                            color = CtPrimary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = CtSpacing.sm),
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(CtSpacing.xxl)) }
        }
    }
}
