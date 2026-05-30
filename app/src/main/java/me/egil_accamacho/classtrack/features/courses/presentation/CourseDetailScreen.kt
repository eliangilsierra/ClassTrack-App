package me.egil_accamacho.classtrack.features.courses.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.PersonAdd
import androidx.compose.material.icons.rounded.QrCode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import me.egil_accamacho.classtrack.navigation.Destination
import me.egil_accamacho.classtrack.ui.components.CtTopAppBar
import me.egil_accamacho.classtrack.ui.components.EmptyState
import me.egil_accamacho.classtrack.ui.components.LoadingState
import me.egil_accamacho.classtrack.ui.components.PrimaryButton
import me.egil_accamacho.classtrack.ui.components.SecondaryButton
import me.egil_accamacho.classtrack.ui.components.StudentCard
import me.egil_accamacho.classtrack.ui.theme.CtBorderLight
import me.egil_accamacho.classtrack.ui.theme.CtError
import me.egil_accamacho.classtrack.ui.theme.CtPrimary
import me.egil_accamacho.classtrack.ui.theme.CtSpacing
import me.egil_accamacho.classtrack.ui.theme.ShapePrimary

@Composable
fun CourseDetailScreen(
    navController: NavController,
    viewModel: CourseDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LifecycleResumeEffect(Unit) {
        viewModel.onEvent(CourseDetailEvent.Load)
        onPauseOrDispose {}
    }

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                is CourseDetailAction.NavigateToLinkStudents  ->
                    navController.navigate(Destination.LinkStudents(action.courseId))
                is CourseDetailAction.NavigateToCreateSession ->
                    navController.navigate(Destination.CreateAttendanceSession(action.courseId))
                is CourseDetailAction.NavigateToReport        ->
                    navController.navigate(Destination.CourseReport(action.courseId))
                CourseDetailAction.NavigateBack               ->
                    navController.popBackStack()
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(CourseDetailEvent.DismissError)
        }
    }

    if (state.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(CourseDetailEvent.DismissDelete) },
            title = { Text("Eliminar curso") },
            text = { Text("¿Estás seguro que deseas eliminar \"${state.course?.name}\"? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(CourseDetailEvent.ConfirmDelete) }) {
                    Text("Eliminar", color = CtError)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(CourseDetailEvent.DismissDelete) }) {
                    Text("Cancelar")
                }
            },
        )
    }

    Scaffold(
        topBar = {
            CtTopAppBar(
                title = state.course?.name ?: "Detalle del Curso",
                onBack = { navController.popBackStack() },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(CourseDetailEvent.RequestDelete) }) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Eliminar curso",
                            tint = CtError,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        if (state.loading) {
            LoadingState(modifier = Modifier.padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(CtSpacing.base),
                verticalArrangement = Arrangement.spacedBy(CtSpacing.base),
            ) {
                // ── Course info card ─────────────────────────────────────────
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = ShapePrimary,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    ) {
                        Column(modifier = Modifier.padding(CtSpacing.base)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.Group,
                                    contentDescription = null,
                                    tint = CtPrimary,
                                    modifier = Modifier.size(20.dp),
                                )
                                Text(
                                    text = " ${state.course?.studentCount ?: 0} estudiantes",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                                    color = MaterialTheme.colorScheme.onBackground,
                                )
                            }
                            if (state.course?.description?.isNotBlank() == true) {
                                Spacer(modifier = Modifier.height(CtSpacing.sm))
                                Text(
                                    text = state.course!!.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }

                // ── Actions ──────────────────────────────────────────────────
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(CtSpacing.sm)) {
                        PrimaryButton(
                            text = "Iniciar Sesión de Asistencia",
                            onClick = { viewModel.onEvent(CourseDetailEvent.NavigateToCreateSession) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(CtSpacing.sm)) {
                            SecondaryButton(
                                text = "Vincular Estudiantes",
                                onClick = { viewModel.onEvent(CourseDetailEvent.NavigateToLinkStudents) },
                                modifier = Modifier.weight(1f),
                            )
                            SecondaryButton(
                                text = "Ver Reportes",
                                onClick = { viewModel.onEvent(CourseDetailEvent.NavigateToReport) },
                                modifier = Modifier.weight(1f),
                            )
                        }
                    }
                }

                // ── Students header ──────────────────────────────────────────
                item {
                    HorizontalDivider(color = CtBorderLight)
                    Spacer(modifier = Modifier.height(CtSpacing.sm))
                    Text(
                        text = "Estudiantes vinculados",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.height(CtSpacing.sm))
                }

                // ── Students list ────────────────────────────────────────────
                if (state.studentsLoading) {
                    item { LoadingState() }
                } else if (state.students.isEmpty()) {
                    item {
                        EmptyState(
                            title = "Sin estudiantes",
                            subtitle = "Vincula estudiantes usando el botón \"Vincular Estudiantes\"",
                            icon = Icons.Rounded.PersonAdd,
                        )
                    }
                } else {
                    items(state.students, key = { it.id }) { student ->
                        StudentCard(
                            studentName = student.fullName,
                            email = student.studentCode,
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(CtSpacing.xl)) }
            }
        }
    }
}
