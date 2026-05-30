package me.egil_accamacho.classtrack.features.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import me.egil_accamacho.classtrack.navigation.Destination
import me.egil_accamacho.classtrack.navigation.RootViewModel
import me.egil_accamacho.classtrack.ui.theme.ClasstrackTheme
import me.egil_accamacho.classtrack.ui.theme.CtPrimary
import me.egil_accamacho.classtrack.ui.theme.CtSpacing

/**
 * Splash Screen — Figma node 1:1142.
 *
 * Shows the ClassTrack logo + app name + loading spinner while [RootViewModel]
 * resolves the DataStore session. Navigates to Login or Home as soon as the
 * post-splash destination is ready, removing itself from the back stack.
 */
@Composable
fun SplashScreen(
    navController: NavController,
    rootViewModel: RootViewModel = hiltViewModel(),
) {
    val postSplashDest by rootViewModel.postSplashDestination.collectAsStateWithLifecycle()

    LaunchedEffect(postSplashDest) {
        postSplashDest?.let { dest ->
            navController.navigate(dest) {
                popUpTo<Destination.Splash> { inclusive = true }
            }
        }
    }

    SplashContent()
}

@Composable
private fun SplashContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Logo circle with school icon
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(CtPrimary),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.School,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(52.dp),
                )
            }

            Spacer(modifier = Modifier.height(CtSpacing.lg))

            Text(
                text = "ClassTrack",
                style = MaterialTheme.typography.displayLarge,
                color = CtPrimary,
            )

            Text(
                text = "Gestión de Asistencia",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(CtSpacing.xxxl))

            CircularProgressIndicator(
                color = CtPrimary,
                modifier = Modifier.size(32.dp),
                strokeWidth = 3.dp,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC)
@Composable
private fun SplashPreview() {
    ClasstrackTheme {
        SplashContent()
    }
}
