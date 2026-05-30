package me.egil_accamacho.classtrack.features.attendance.presentation

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
import me.egil_accamacho.classtrack.ui.components.PrimaryButton
import me.egil_accamacho.classtrack.ui.theme.CtSpacing
import me.egil_accamacho.classtrack.ui.theme.CtSuccess
import me.egil_accamacho.classtrack.ui.theme.ShapePrimary

@Composable
fun AttendanceSuccessScreen(
    navController: NavController,
    viewModel: AttendanceSuccessViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                AttendanceSuccessAction.NavigateHome ->
                    navController.navigate(Destination.HomeStudent) {
                        popUpTo(Destination.HomeStudent) { inclusive = false }
                    }
            }
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(CtSpacing.base),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // ── Success icon ──────────────────────────────────────────────────
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
                text = "Asistencia registrada",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(CtSpacing.sm))

            Text(
                text = "Tu asistencia ha sido registrada correctamente.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            // ── Registered time card ──────────────────────────────────────────
            if (state.registeredAt.isNotBlank()) {
                Spacer(modifier = Modifier.height(CtSpacing.lg))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = ShapePrimary,
                    colors = CardDefaults.cardColors(
                        containerColor = CtSuccess.copy(alpha = 0.08f),
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(CtSpacing.base),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Hora de registro",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(CtSpacing.xs))
                        Text(
                            text = formatRegisteredAt(state.registeredAt),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                            ),
                            color = CtSuccess,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(CtSpacing.xl))

            PrimaryButton(
                text = "Ir al inicio",
                onClick = { viewModel.onEvent(AttendanceSuccessEvent.GoHome) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private fun formatRegisteredAt(raw: String): String = runCatching {
    // Input: "2026-05-30T18:05:00" → Output: "18:05"
    val timePart = raw.substringAfter("T").take(5)
    timePart
}.getOrDefault(raw)
