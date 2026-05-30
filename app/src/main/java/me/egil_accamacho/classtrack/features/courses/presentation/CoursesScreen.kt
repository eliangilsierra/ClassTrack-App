package me.egil_accamacho.classtrack.features.courses.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import me.egil_accamacho.classtrack.navigation.Destination
import me.egil_accamacho.classtrack.ui.components.CourseCard
import me.egil_accamacho.classtrack.ui.components.CtTopAppBar
import me.egil_accamacho.classtrack.ui.components.EmptyState
import me.egil_accamacho.classtrack.ui.components.LoadingState
import me.egil_accamacho.classtrack.ui.theme.CtError
import me.egil_accamacho.classtrack.ui.theme.CtPrimary
import me.egil_accamacho.classtrack.ui.theme.CtSpacing

@Composable
fun CoursesScreen(
    navController: NavController,
    viewModel: CoursesViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LifecycleResumeEffect(Unit) {
        viewModel.onEvent(CoursesEvent.Load)
        onPauseOrDispose {}
    }

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                CoursesAction.NavigateToCreate       -> navController.navigate(Destination.CreateCourse)
                is CoursesAction.NavigateToDetail    -> navController.navigate(Destination.CourseDetail(action.courseId))
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(CoursesEvent.DismissError)
        }
    }

    if (state.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(CoursesEvent.DismissDelete) },
            title = { Text("Eliminar curso") },
            text = { Text("¿Estás seguro que deseas eliminar \"${state.courseToDelete?.name}\"? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(CoursesEvent.ConfirmDelete) }) {
                    Text("Eliminar", color = CtError)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(CoursesEvent.DismissDelete) }) {
                    Text("Cancelar")
                }
            },
        )
    }

    Scaffold(
        topBar = { CtTopAppBar(title = "Mis Cursos") },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(CoursesEvent.NavigateToCreate) },
                containerColor = CtPrimary,
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Crear curso", tint = androidx.compose.ui.graphics.Color.White)
            }
        },
    ) { padding ->
        when {
            state.loading && state.courses.isEmpty() -> LoadingState(modifier = Modifier.padding(padding))
            state.courses.isEmpty() -> EmptyState(
                title = "Sin cursos",
                subtitle = "Crea tu primer curso usando el botón +",
                modifier = Modifier.padding(padding).fillMaxSize(),
            )
            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(CtSpacing.base),
                verticalArrangement = Arrangement.spacedBy(CtSpacing.md),
            ) {
                items(state.courses, key = { it.id }) { course ->
                    CourseCard(
                        courseName = course.name,
                        studentCount = course.studentCount,
                        lastSession = "—",
                        onClick = { viewModel.onEvent(CoursesEvent.NavigateToDetail(course.id)) },
                        onDelete = { viewModel.onEvent(CoursesEvent.RequestDelete(course)) },
                    )
                }
            }
        }
    }
}
