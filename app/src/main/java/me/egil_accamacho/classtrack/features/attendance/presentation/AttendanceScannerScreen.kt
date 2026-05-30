@file:Suppress("UnsafeOptInUsageError")

package me.egil_accamacho.classtrack.features.attendance.presentation

import android.Manifest
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import me.egil_accamacho.classtrack.core.qr.BarcodeAnalyzer
import me.egil_accamacho.classtrack.navigation.Destination
import me.egil_accamacho.classtrack.ui.components.PrimaryButton
import me.egil_accamacho.classtrack.ui.components.QrScannerOverlay
import me.egil_accamacho.classtrack.ui.components.SecondaryButton
import me.egil_accamacho.classtrack.ui.theme.CtPrimary
import me.egil_accamacho.classtrack.ui.theme.CtSpacing
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AttendanceScannerScreen(
    navController: NavController,
    viewModel: AttendanceScannerViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val permissions = rememberMultiplePermissionsState(
        listOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION),
    )

    LaunchedEffect(Unit) {
        if (!permissions.allPermissionsGranted) permissions.launchMultiplePermissionRequest()
        viewModel.actions.collect { action ->
            when (action) {
                is AttendanceScannerAction.NavigateToSuccess ->
                    navController.navigate(
                        Destination.AttendanceSuccess(
                            sessionId    = action.sessionId,
                            registeredAt = action.registeredAt,
                        ),
                    ) {
                        popUpTo(Destination.AttendanceScanner) { inclusive = true }
                    }
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(AttendanceScannerEvent.DismissError)
        }
    }

    when {
        permissions.allPermissionsGranted -> {
            Box(modifier = Modifier.fillMaxSize()) {
                ScannerCameraContent(
                    onQrDetected = { viewModel.onEvent(AttendanceScannerEvent.QrDetected(it)) },
                    onDismiss    = { navController.popBackStack() },
                )
                if (state.processing) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.45f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = Color.White,
                            strokeWidth = 3.dp,
                        )
                    }
                }
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.align(Alignment.BottomCenter),
                )
            }
        }
        permissions.permissions.none { it.status.isGranted } -> {
            PermissionDeniedContent(
                message = "ClassTrack necesita acceso a la cámara y ubicación para registrar tu asistencia.",
                onRequest = { permissions.launchMultiplePermissionRequest() },
                onDismiss = { navController.popBackStack() },
            )
        }
        else -> {
            val missing = permissions.permissions.filter { !it.status.isGranted }
                .joinToString(", ") { p ->
                    when (p.permission) {
                        Manifest.permission.CAMERA -> "cámara"
                        else -> "ubicación"
                    }
                }
            PermissionDeniedContent(
                message = "Permiso de $missing requerido. Habilítalo en la configuración del dispositivo.",
                onRequest = { permissions.launchMultiplePermissionRequest() },
                onDismiss = { navController.popBackStack() },
                isPermanentlyDenied = true,
            )
        }
    }
}

@ExperimentalGetImage
@Composable
private fun ScannerCameraContent(
    onQrDetected: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                val future = ProcessCameraProvider.getInstance(ctx)
                future.addListener({
                    val provider = future.get()
                    val preview = Preview.Builder().build().also {
                        it.surfaceProvider = previewView.surfaceProvider
                    }
                    val analysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also { it.setAnalyzer(cameraExecutor, BarcodeAnalyzer(onDetected = onQrDetected)) }
                    provider.unbindAll()
                    provider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, analysis)
                }, ContextCompat.getMainExecutor(ctx))
                previewView
            },
            modifier = Modifier.fillMaxSize(),
        )
        QrScannerOverlay(
            instruction = "Escanea el QR de asistencia que muestra tu profesor",
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun PermissionDeniedContent(
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
        Icon(
            imageVector = Icons.Rounded.LocationOff,
            contentDescription = null,
            tint = CtPrimary,
            modifier = Modifier.size(48.dp),
        )
        Spacer(Modifier.height(CtSpacing.base))
        Text(
            text = "Permisos requeridos",
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
            PrimaryButton(text = "Conceder permisos", onClick = onRequest)
            Spacer(Modifier.height(CtSpacing.sm))
        }
        SecondaryButton(text = "Cancelar", onClick = onDismiss)
    }
}
