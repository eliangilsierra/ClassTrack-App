package me.egil_accamacho.classtrack.ui.components

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import me.egil_accamacho.classtrack.ui.theme.ClasstrackTheme
import me.egil_accamacho.classtrack.ui.theme.CtError

/**
 * Error-styled Snackbar host. Place inside a [Scaffold]'s `snackbarHost` slot.
 *
 * Usage in a screen:
 * ```
 * val snackbarHostState = remember { SnackbarHostState() }
 * // Show an error:
 * LaunchedEffect(uiState.error) {
 *     uiState.error?.let { snackbarHostState.showError(it) }
 * }
 * Scaffold(
 *     snackbarHost = { ErrorSnackbarHost(snackbarHostState) },
 *     ...
 * )
 * ```
 */
@Composable
fun ErrorSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
    ) { data ->
        ErrorSnackbar(data = data)
    }
}

/** A single error Snackbar with red container and white text. */
@Composable
fun ErrorSnackbar(data: SnackbarData) {
    Snackbar(
        snackbarData = data,
        containerColor = CtError,
        contentColor = Color.White,
        actionColor = Color.White,
        dismissActionContentColor = Color.White.copy(alpha = 0.7f),
    )
}

/**
 * Extension to [SnackbarHostState] for ergonomic error display.
 * Automatically trims and shows the message with a fixed duration.
 *
 * @param message       Error message to show
 * @param actionLabel   Optional Snackbar action label (e.g. "Reintentar")
 * @return [SnackbarResult] — useful if callers need to detect action tap
 */
suspend fun SnackbarHostState.showError(
    message: String,
    actionLabel: String? = null,
): SnackbarResult = showSnackbar(
    message = message.trim(),
    actionLabel = actionLabel,
    duration = SnackbarDuration.Long,
    withDismissAction = true,
)

// ── Preview ───────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun ErrorSnackbarPreview() {
    ClasstrackTheme {
        val hostState = remember { SnackbarHostState() }
        LaunchedEffect(Unit) {
            hostState.showError("No se pudo conectar al servidor. Verifica tu conexión.")
        }
        ErrorSnackbarHost(hostState = hostState)
    }
}
