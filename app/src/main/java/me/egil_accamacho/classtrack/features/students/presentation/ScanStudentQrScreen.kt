@file:Suppress("UnsafeOptInUsageError")

package me.egil_accamacho.classtrack.features.students.presentation

import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import me.egil_accamacho.classtrack.core.permissions.CameraPermissionRequester
import me.egil_accamacho.classtrack.core.qr.BarcodeAnalyzer
import me.egil_accamacho.classtrack.navigation.Destination
import me.egil_accamacho.classtrack.ui.components.QrScannerOverlay
import java.util.concurrent.Executors

@Composable
fun ScanStudentQrScreen(
    navController: NavController,
    viewModel: ScanStudentQrViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                is ScanStudentQrAction.NavigateToSuccess ->
                    navController.navigate(
                        Destination.StudentLinkedSuccess(
                            studentId = action.studentId,
                            courseId = action.courseId,
                        ),
                    ) {
                        popUpTo(Destination.ScanStudentQr(action.courseId)) { inclusive = true }
                    }
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(ScanStudentQrEvent.DismissError)
        }
    }

    CameraPermissionRequester(
        onDismiss = { navController.popBackStack() },
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            CameraQrContent(
                processing = state.processing,
                onQrDetected = { content ->
                    viewModel.onEvent(ScanStudentQrEvent.QrDetected(content))
                },
                onDismiss = { navController.popBackStack() },
            )
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@ExperimentalGetImage
@Composable
private fun CameraQrContent(
    processing: Boolean,
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
                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.surfaceProvider = previewView.surfaceProvider
                    }
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also { analysis ->
                            analysis.setAnalyzer(
                                cameraExecutor,
                                BarcodeAnalyzer(onDetected = onQrDetected),
                            )
                        }
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageAnalysis,
                    )
                }, ContextCompat.getMainExecutor(ctx))
                previewView
            },
            modifier = Modifier.fillMaxSize(),
        )

        QrScannerOverlay(
            instruction = "Escanea el QR del estudiante para vincularlo al curso",
            onDismiss = onDismiss,
        )

        if (processing) {
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
    }
}
