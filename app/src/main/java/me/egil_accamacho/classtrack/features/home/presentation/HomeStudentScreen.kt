package me.egil_accamacho.classtrack.features.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QrCodeScanner
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import me.egil_accamacho.classtrack.navigation.Destination
import me.egil_accamacho.classtrack.ui.components.CtTopAppBar
import me.egil_accamacho.classtrack.ui.components.PrimaryButton
import me.egil_accamacho.classtrack.ui.theme.CtPrimary
import me.egil_accamacho.classtrack.ui.theme.CtPrimaryDark
import me.egil_accamacho.classtrack.ui.theme.CtSpacing

@Composable
fun HomeStudentScreen(
    navController: NavController,
    viewModel: HomeStudentViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                HomeStudentAction.NavigateToScanner -> navController.navigate(Destination.AttendanceScanner)
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(HomeStudentEvent.DismissError)
        }
    }

    Scaffold(
        topBar = {
            val greeting = if (state.studentName.isNotBlank())
                "Hola, ${state.studentName.split(" ").firstOrNull() ?: ""}"
            else "Hola, Estudiante"
            CtTopAppBar(title = greeting)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(CtSpacing.base),
            verticalArrangement = Arrangement.spacedBy(CtSpacing.base),
        ) {
            // ── QR scan hero card ─────────────────────────────────────────────
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(CtPrimary, CtPrimaryDark),
                                ),
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                            )
                            .padding(CtSpacing.xl),
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.QrCodeScanner,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(40.dp),
                                )
                            }

                            Spacer(modifier = Modifier.height(CtSpacing.base))

                            Text(
                                text = "Registrar Asistencia",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = Color.White,
                            )

                            Spacer(modifier = Modifier.height(CtSpacing.sm))

                            Text(
                                text = "Escanea el QR de tu docente para registrar tu asistencia",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.85f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            )

                            Spacer(modifier = Modifier.height(CtSpacing.lg))

                            PrimaryButton(
                                text = "Escanear QR",
                                onClick = { viewModel.onEvent(HomeStudentEvent.NavigateToScanner) },
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                }
            }

            // ── Info chips ────────────────────────────────────────────────────
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(CtSpacing.sm),
                ) {
                    InfoChip(
                        label = "Cámara requerida",
                        modifier = Modifier.weight(1f),
                    )
                    InfoChip(
                        label = "GPS requerido",
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(CtSpacing.xxl)) }
        }
    }
}

@Composable
private fun InfoChip(label: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(CtPrimary.copy(alpha = 0.08f))
            .padding(horizontal = CtSpacing.md, vertical = CtSpacing.sm),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            color = CtPrimary,
        )
    }
}
