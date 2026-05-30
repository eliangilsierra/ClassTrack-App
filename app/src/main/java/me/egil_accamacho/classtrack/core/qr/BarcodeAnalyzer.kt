package me.egil_accamacho.classtrack.core.qr

import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

/**
 * CameraX [ImageAnalysis.Analyzer] that uses ML Kit to detect QR codes.
 * Throttles callbacks to one per [throttleMs] milliseconds to avoid
 * flooding the ViewModel with repeated detections of the same code.
 *
 * @param throttleMs Minimum milliseconds between successive detections (default 1 500 ms)
 * @param onDetected Invoked on the calling thread with the raw QR string
 */
@ExperimentalGetImage
class BarcodeAnalyzer(
    private val throttleMs: Long = 1_500L,
    private val onDetected: (String) -> Unit,
) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build(),
    )
    private var lastDetectedAt = 0L

    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }
        val now = System.currentTimeMillis()
        if (now - lastDetectedAt < throttleMs) {
            imageProxy.close()
            return
        }
        val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        scanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                barcodes.firstOrNull()?.rawValue?.let { raw ->
                    lastDetectedAt = System.currentTimeMillis()
                    onDetected(raw)
                }
            }
            .addOnCompleteListener { imageProxy.close() }
    }
}
