package me.egil_accamacho.classtrack.core.permissions

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import me.egil_accamacho.classtrack.ui.components.PrimaryButton
import me.egil_accamacho.classtrack.ui.components.SecondaryButton
import me.egil_accamacho.classtrack.ui.theme.CtSpacing

/**
 * Wrapper that handles the camera permission lifecycle.
 * Shows [content] when granted, a rationale/denied UI otherwise.
 *
 * @param onPermissionGranted Callback invoked once after the permission is first granted
 * @param onDismiss           Called when the user cancels the permission flow
 * @param content             Content to display while the camera permission is granted
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionRequester(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        if (!cameraPermission.status.isGranted) {
            cameraPermission.launchPermissionRequest()
        }
    }

    when {
        cameraPermission.status.isGranted -> content()
        cameraPermission.status.shouldShowRationale -> {
            PermissionRationaleScreen(
                message = "ClassTrack necesita acceso a la cámara para escanear los códigos QR.",
                onRequest = { cameraPermission.launchPermissionRequest() },
                onDismiss = onDismiss,
            )
        }
        else -> {
            PermissionRationaleScreen(
                message = "El permiso de cámara fue denegado. Habilítalo en la configuración del dispositivo para continuar.",
                onRequest = { cameraPermission.launchPermissionRequest() },
                onDismiss = onDismiss,
                isPermanentlyDenied = true,
            )
        }
    }
}

@Composable
private fun PermissionRationaleScreen(
    message: String,
    onRequest: () -> Unit,
    onDismiss: () -> Unit,
    isPermanentlyDenied: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(CtSpacing.xl),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        androidx.compose.material3.Icon(
            imageVector = Icons.Rounded.CameraAlt,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(CtSpacing.base),
        )
        Spacer(Modifier.height(CtSpacing.base))
        Text(
            text = "Permiso de Cámara",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(CtSpacing.sm))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(CtSpacing.xl))
        if (!isPermanentlyDenied) {
            PrimaryButton(text = "Conceder permiso", onClick = onRequest)
        }
        Spacer(Modifier.height(CtSpacing.sm))
        SecondaryButton(text = "Cancelar", onClick = onDismiss)
    }
}
