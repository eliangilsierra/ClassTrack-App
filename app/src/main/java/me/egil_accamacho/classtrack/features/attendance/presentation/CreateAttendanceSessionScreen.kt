package me.egil_accamacho.classtrack.features.attendance.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.EventNote
import androidx.compose.material.icons.rounded.QrCode2
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import me.egil_accamacho.classtrack.navigation.Destination
import me.egil_accamacho.classtrack.ui.components.CtTopAppBar
import me.egil_accamacho.classtrack.ui.components.PrimaryButton
import me.egil_accamacho.classtrack.ui.theme.CtPrimary
import me.egil_accamacho.classtrack.ui.theme.CtSpacing
import me.egil_accamacho.classtrack.ui.theme.ShapePrimary

@Composable
fun CreateAttendanceSessionScreen(
    navController: NavController,
    viewModel: CreateAttendanceSessionViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                is CreateAttendanceSessionAction.NavigateToQr ->
                    navController.navigate(
                        Destination.AttendanceQr(
                            sessionId = action.sessionId,
                            qrToken   = action.qrToken,
                            expiresAt = action.expiresAt,
                        ),
                    )
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(CreateAttendanceSessionEvent.DismissError)
        }
    }

    Scaffold(
        topBar = {
            CtTopAppBar(
                title = "Iniciar Sesión",
                onBack = { navController.popBackStack() },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(CtSpacing.base),
            verticalArrangement = Arrangement.spacedBy(CtSpacing.base),
        ) {
            // ── Course card ───────────────────────────────────────────────────
            if (state.courseName.isNotBlank()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = ShapePrimary,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                ) {
                    Row(
                        modifier = Modifier.padding(CtSpacing.base),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.EventNote,
                            contentDescription = null,
                            tint = CtPrimary,
                            modifier = Modifier.size(24.dp),
                        )
                        Spacer(modifier = Modifier.size(CtSpacing.sm))
                        Text(
                            text = state.courseName,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }
            }

            // ── Instruction card ──────────────────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = ShapePrimary,
                colors = CardDefaults.cardColors(
                    containerColor = CtPrimary.copy(alpha = 0.06f),
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Column(
                    modifier = Modifier.padding(CtSpacing.base),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.QrCode2,
                        contentDescription = null,
                        tint = CtPrimary,
                        modifier = Modifier.size(48.dp),
                    )
                    Spacer(modifier = Modifier.height(CtSpacing.md))
                    Text(
                        text = "Se generará un QR de asistencia",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Spacer(modifier = Modifier.height(CtSpacing.sm))
                    Text(
                        text = "Los estudiantes deberán escanear el código QR con su app ClassTrack para registrar su asistencia.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                text = "Generar QR de Asistencia",
                onClick = { viewModel.onEvent(CreateAttendanceSessionEvent.CreateSession) },
                loading = state.loading,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(CtSpacing.base))
        }
    }
}
