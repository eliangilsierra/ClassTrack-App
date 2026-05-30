package me.egil_accamacho.classtrack.features.profile.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.Badge
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import me.egil_accamacho.classtrack.navigation.Destination
import me.egil_accamacho.classtrack.ui.components.CtTopAppBar
import me.egil_accamacho.classtrack.ui.components.LoadingState
import me.egil_accamacho.classtrack.ui.theme.CtBorderLight
import me.egil_accamacho.classtrack.ui.theme.CtError
import me.egil_accamacho.classtrack.ui.theme.CtPrimary
import me.egil_accamacho.classtrack.ui.theme.CtSpacing
import me.egil_accamacho.classtrack.ui.theme.ShapePrimary

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                ProfileAction.NavigateToDigitalId ->
                    navController.navigate(Destination.DigitalId)
                ProfileAction.NavigateToLogin ->
                    navController.navigate(Destination.Login) {
                        popUpTo(0) { inclusive = true }
                    }
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(ProfileEvent.DismissError)
        }
    }

    if (state.showLogoutDialog) {
        LogoutConfirmDialog(
            onConfirm = { viewModel.onEvent(ProfileEvent.ConfirmLogout) },
            onDismiss = { viewModel.onEvent(ProfileEvent.DismissLogoutDialog) },
        )
    }

    Scaffold(
        topBar = { CtTopAppBar(title = "Mi Perfil") },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        if (state.loading && state.profile == null) {
            LoadingState(modifier = Modifier.padding(padding))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState()),
            ) {
                // ── Profile header card ────────────────────────────────────
                ProfileHeaderCard(
                    fullName = state.profile?.fullName ?: "",
                    email = state.profile?.email ?: "",
                    roleLabel = state.profile?.roleLabel ?: "",
                    modifier = Modifier.padding(CtSpacing.base),
                )

                Spacer(modifier = Modifier.height(CtSpacing.sm))

                // ── Actions section ────────────────────────────────────────
                Text(
                    text = "Acciones",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = CtSpacing.base, vertical = CtSpacing.sm),
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = CtSpacing.base),
                    shape = ShapePrimary,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                ) {
                    ProfileActionRow(
                        icon = Icons.Rounded.Badge,
                        label = "Mi Identificación Digital",
                        description = "Ver QR permanente para identificación",
                        onClick = { viewModel.onEvent(ProfileEvent.NavigateToDigitalId) },
                    )

                    HorizontalDivider(
                        color = CtBorderLight,
                        modifier = Modifier.padding(horizontal = CtSpacing.base),
                    )

                    ProfileActionRow(
                        icon = Icons.AutoMirrored.Rounded.Logout,
                        label = "Cerrar sesión",
                        description = "Salir de tu cuenta ClassTrack",
                        iconTint = CtError,
                        labelColor = CtError,
                        onClick = { viewModel.onEvent(ProfileEvent.ShowLogoutDialog) },
                        showChevron = false,
                    )
                }

                Spacer(modifier = Modifier.height(CtSpacing.xl))
            }
        }
    }
}

// ── Private composables ───────────────────────────────────────────────────────

@Composable
private fun ProfileHeaderCard(
    fullName: String,
    email: String,
    roleLabel: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = ShapePrimary,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(CtPrimary.copy(alpha = 0.08f), Color.Transparent),
                    ),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(CtSpacing.xl),
            ) {
                // Avatar with initials
                val initials = fullName.trim().split("\\s+".toRegex())
                    .take(2).mapNotNull { it.firstOrNull()?.uppercaseChar() }
                    .joinToString("").ifEmpty { "?" }

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(CtPrimary),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = initials,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                        ),
                        color = Color.White,
                    )
                }

                Spacer(modifier = Modifier.height(CtSpacing.md))

                Text(
                    text = fullName.ifEmpty { "—" },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Spacer(modifier = Modifier.height(CtSpacing.sm))

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(CtPrimary.copy(alpha = 0.10f))
                        .border(1.dp, CtPrimary.copy(alpha = 0.20f), CircleShape)
                        .padding(horizontal = CtSpacing.md, vertical = CtSpacing.xs),
                ) {
                    Text(
                        text = roleLabel.ifEmpty { "—" },
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
                        color = CtPrimary,
                    )
                }

                Spacer(modifier = Modifier.height(CtSpacing.sm))

                Text(
                    text = email.ifEmpty { "—" },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun ProfileActionRow(
    icon: ImageVector,
    label: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconTint: Color = CtPrimary,
    labelColor: Color = MaterialTheme.colorScheme.onBackground,
    showChevron: Boolean = true,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(CtSpacing.base),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconTint.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp),
            )
        }

        Spacer(modifier = Modifier.width(CtSpacing.md))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = labelColor,
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        if (showChevron) {
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}
