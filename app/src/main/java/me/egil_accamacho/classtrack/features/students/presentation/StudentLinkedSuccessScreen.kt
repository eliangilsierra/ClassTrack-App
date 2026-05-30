package me.egil_accamacho.classtrack.features.students.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import me.egil_accamacho.classtrack.navigation.Destination
import me.egil_accamacho.classtrack.ui.components.CtTopAppBar
import me.egil_accamacho.classtrack.ui.components.LoadingState
import me.egil_accamacho.classtrack.ui.components.PrimaryButton
import me.egil_accamacho.classtrack.ui.components.SecondaryButton
import me.egil_accamacho.classtrack.ui.theme.CtSpacing
import me.egil_accamacho.classtrack.ui.theme.CtSuccess
import me.egil_accamacho.classtrack.ui.theme.ShapePrimary

@Composable
fun StudentLinkedSuccessScreen(
    navController: NavController,
    viewModel: StudentLinkedSuccessViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                is StudentLinkedSuccessAction.NavigateToScan ->
                    navController.navigate(Destination.ScanStudentQr(action.courseId)) {
                        popUpTo(Destination.ScanStudentQr(action.courseId)) { inclusive = true }
                    }
                is StudentLinkedSuccessAction.NavigateToCourseDetail ->
                    navController.navigate(Destination.CourseDetail(action.courseId)) {
                        popUpTo(Destination.CourseDetail(action.courseId)) { inclusive = true }
                    }
            }
        }
    }

    Scaffold(
        topBar = {
            CtTopAppBar(
                title = "Estudiante Vinculado",
                onBack = { navController.popBackStack() },
            )
        },
    ) { padding ->
        if (state.loading) {
            LoadingState(modifier = Modifier.padding(padding))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(CtSpacing.base),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                // ── Success icon ──────────────────────────────────────────────
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .background(
                            color = CtSuccess.copy(alpha = 0.12f),
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = CtSuccess,
                        modifier = Modifier.size(56.dp),
                    )
                }

                Spacer(modifier = Modifier.height(CtSpacing.lg))

                Text(
                    text = "¡Vinculación exitosa!",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(CtSpacing.sm))

                Text(
                    text = "El estudiante ha sido vinculado al curso correctamente.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )

                // ── Student info card ─────────────────────────────────────────
                state.student?.let { student ->
                    Spacer(modifier = Modifier.height(CtSpacing.lg))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = ShapePrimary,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(CtSpacing.base),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = student.fullName,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                ),
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center,
                            )
                            Spacer(modifier = Modifier.height(CtSpacing.xs))
                            Text(
                                text = student.studentCode,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(CtSpacing.xl))

                PrimaryButton(
                    text = "Escanear otro estudiante",
                    onClick = { viewModel.onEvent(StudentLinkedSuccessEvent.ScanAnother) },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(CtSpacing.sm))

                SecondaryButton(
                    text = "Volver al curso",
                    onClick = { viewModel.onEvent(StudentLinkedSuccessEvent.BackToCourse) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
