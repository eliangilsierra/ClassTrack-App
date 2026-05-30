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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import me.egil_accamacho.classtrack.navigation.Destination
import me.egil_accamacho.classtrack.ui.components.CtTopAppBar
import me.egil_accamacho.classtrack.ui.components.LoadingState
import me.egil_accamacho.classtrack.ui.components.QrDisplay
import me.egil_accamacho.classtrack.ui.components.SecondaryButton
import me.egil_accamacho.classtrack.ui.theme.CtError
import me.egil_accamacho.classtrack.ui.theme.CtPrimary
import me.egil_accamacho.classtrack.ui.theme.CtSpacing
import me.egil_accamacho.classtrack.ui.theme.ShapePrimary

@Composable
fun AttendanceQrScreen(
    navController: NavController,
    viewModel: AttendanceQrViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showCloseDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                is AttendanceQrAction.NavigateToReport ->
                    navController.navigate(Destination.AttendanceReport(action.sessionId)) {
                        popUpTo<Destination.AttendanceQr> { inclusive = true }
                    }
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(AttendanceQrEvent.DismissError)
        }
    }

    if (showCloseDialog) {
        AlertDialog(
            onDismissRequest = { showCloseDialog = false },
            title = { Text("Cerrar sesión de asistencia") },
            text = { Text("¿Deseas cerrar esta sesión? Los estudiantes ya no podrán registrar su asistencia.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCloseDialog = false
                        viewModel.onEvent(AttendanceQrEvent.CloseSession)
                    },
                ) {
                    Text("Cerrar sesión", color = CtError)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCloseDialog = false }) {
                    Text("Cancelar")
                }
            },
        )
    }

    Scaffold(
        topBar = {
            CtTopAppBar(
                title = "QR de Asistencia",
                onBack = { navController.popBackStack() },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        if (state.loading) {
            LoadingState(modifier = Modifier.padding(padding))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(CtSpacing.base)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(CtSpacing.base),
            ) {
                // ── QR code with countdown ────────────────────────────────────
                QrDisplay(
                    bitmap = state.qrBitmap,
                    remainingSeconds = state.remainingSeconds,
                    totalSeconds = state.remainingSeconds.coerceAtLeast(1),
                    modifier = Modifier.fillMaxWidth(0.78f),
                )

                // ── Live attendees card ───────────────────────────────────────
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = ShapePrimary,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(CtSpacing.base),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(CtSpacing.sm),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Group,
                            contentDescription = null,
                            tint = CtPrimary,
                            modifier = Modifier.size(20.dp),
                        )
                        Column {
                            Text(
                                text = "${state.records.size} asistentes registrados",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                            if (state.records.isNotEmpty()) {
                                Text(
                                    text = "Último: ${state.records.last().studentName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(CtSpacing.lg))

                SecondaryButton(
                    text = "Cerrar Sesión de Asistencia",
                    onClick = { showCloseDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(CtSpacing.base))
            }
        }
    }
}
