package me.egil_accamacho.classtrack.features.reports.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Percent
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import me.egil_accamacho.classtrack.ui.components.CtTopAppBar
import me.egil_accamacho.classtrack.ui.components.EmptyState
import me.egil_accamacho.classtrack.ui.components.LoadingState
import me.egil_accamacho.classtrack.ui.components.MetricCard
import me.egil_accamacho.classtrack.ui.components.StudentCard
import me.egil_accamacho.classtrack.ui.theme.CtError
import me.egil_accamacho.classtrack.ui.theme.CtSpacing
import me.egil_accamacho.classtrack.ui.theme.CtSuccess
import me.egil_accamacho.classtrack.ui.theme.CtWarning

@Composable
fun AttendanceReportScreen(
    navController: NavController,
    viewModel: AttendanceReportViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(AttendanceReportEvent.DismissError)
        }
    }

    Scaffold(
        topBar = {
            CtTopAppBar(
                title = "Reporte de Sesión",
                onBack = { navController.popBackStack() },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        if (state.loading && state.report == null) {
            LoadingState(modifier = Modifier.padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(CtSpacing.base),
                verticalArrangement = Arrangement.spacedBy(CtSpacing.base),
            ) {
                // ── Metric cards ──────────────────────────────────────────────
                state.report?.let { report ->
                    item {
                        Text(
                            text = "Resumen de asistencia",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }

                    item {
                        androidx.compose.foundation.layout.Row(
                            horizontalArrangement = Arrangement.spacedBy(CtSpacing.sm),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            MetricCard(
                                label = "Total",
                                value = "${report.totalStudents}",
                                icon  = Icons.Rounded.Group,
                                modifier = Modifier.weight(1f),
                            )
                            MetricCard(
                                label = "Presentes",
                                value = "${report.attendees}",
                                icon  = Icons.Rounded.CheckCircle,
                                modifier = Modifier.weight(1f),
                                trendColor = CtSuccess,
                            )
                        }
                    }

                    item {
                        androidx.compose.foundation.layout.Row(
                            horizontalArrangement = Arrangement.spacedBy(CtSpacing.sm),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            MetricCard(
                                label = "Ausentes",
                                value = "${report.absent}",
                                icon  = Icons.Rounded.Cancel,
                                modifier = Modifier.weight(1f),
                                trendColor = CtError,
                            )
                            MetricCard(
                                label = "Asistencia",
                                value = "${report.attendancePercent}%",
                                icon  = Icons.Rounded.Percent,
                                modifier = Modifier.weight(1f),
                                trendColor = when {
                                    report.attendancePercent >= 80 -> CtSuccess
                                    report.attendancePercent >= 60 -> CtWarning
                                    else -> CtError
                                },
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(CtSpacing.sm)) }
                }

                // ── Attendees list ────────────────────────────────────────────
                if (state.records.isNotEmpty()) {
                    item {
                        Text(
                            text = "Estudiantes presentes (${state.records.size})",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                    items(state.records, key = { it.studentId }) { record ->
                        StudentCard(
                            studentName = record.studentName,
                            email       = "Registrado: ${formatTime(record.registeredAt)}",
                        )
                    }
                } else if (!state.loading) {
                    item {
                        Column {
                            Text(
                                text = "Estudiantes presentes",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                            Spacer(Modifier.height(CtSpacing.base))
                            EmptyState(
                                icon = Icons.Rounded.Group,
                                title = "Sin registros",
                                subtitle = "Ningún estudiante registró asistencia en esta sesión.",
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(CtSpacing.base)) }
            }
        }
    }
}

private fun formatTime(raw: String): String = runCatching {
    raw.substringAfter("T").take(5)
}.onFailure {
    Log.e("AttendanceReportScreen", "Error formatting time: $raw", it)
}.getOrDefault(raw)
