package me.egil_accamacho.classtrack.features.reports.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import me.egil_accamacho.classtrack.ui.components.CtTopAppBar
import me.egil_accamacho.classtrack.ui.components.LoadingState
import me.egil_accamacho.classtrack.ui.components.MetricCard
import me.egil_accamacho.classtrack.ui.theme.CtError
import me.egil_accamacho.classtrack.ui.theme.CtSpacing
import me.egil_accamacho.classtrack.ui.theme.CtSuccess
import me.egil_accamacho.classtrack.ui.theme.CtWarning

@Composable
fun CourseReportScreen(
    navController: NavController,
    viewModel: CourseReportViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(CourseReportEvent.DismissError)
        }
    }

    Scaffold(
        topBar = {
            CtTopAppBar(
                title = "Reporte del Curso",
                onBack = { navController.popBackStack() },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        if (state.loading && state.report == null) {
            LoadingState(modifier = Modifier.padding(padding))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(CtSpacing.base),
                verticalArrangement = Arrangement.spacedBy(CtSpacing.base),
            ) {
                state.report?.let { report ->
                    // ── Course name ───────────────────────────────────────────
                    Text(
                        text = report.courseName,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground,
                    )

                    Spacer(modifier = Modifier.height(CtSpacing.sm))

                    Text(
                        text = "Estadísticas generales",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onBackground,
                    )

                    // ── Metric cards ──────────────────────────────────────────
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(CtSpacing.sm),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        MetricCard(
                            label    = "Estudiantes",
                            value    = "${report.totalStudents}",
                            icon     = Icons.Rounded.Group,
                            modifier = Modifier.weight(1f),
                        )
                        MetricCard(
                            label      = "Asistencia",
                            value      = "${report.attendanceRate}%",
                            icon       = Icons.Rounded.Percent,
                            modifier   = Modifier.weight(1f),
                            trendColor = when {
                                report.attendanceRate >= 80 -> CtSuccess
                                report.attendanceRate >= 60 -> CtWarning
                                else                        -> CtError
                            },
                        )
                    }
                }
            }
        }
    }
}
