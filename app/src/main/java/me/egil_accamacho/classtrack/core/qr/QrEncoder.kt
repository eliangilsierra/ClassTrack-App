package me.egil_accamacho.classtrack.core.qr

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.egil_accamacho.classtrack.di.DefaultDispatcher
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Encodes a string into a QR code [Bitmap] using ZXing.
 * Encoding runs on [DefaultDispatcher] (CPU-bound work).
 *
 * Usage:
 * ```kotlin
 * val bitmap = qrEncoder.encode(qrContent).getOrNull()
 * ```
 */
@Singleton
class QrEncoder @Inject constructor(
    @param:DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) {

    suspend fun encode(content: String, sizePx: Int = 512): Result<Bitmap> =
        withContext(dispatcher) {
            runCatching {
                val hints = mapOf(EncodeHintType.MARGIN to 1)
                val writer = QRCodeWriter()
                val matrix = writer.encode(content, BarcodeFormat.QR_CODE, sizePx, sizePx, hints)

                val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
                for (x in 0 until sizePx) {
                    for (y in 0 until sizePx) {
                        bitmap.setPixel(x, y, if (matrix[x, y]) Color.BLACK else Color.WHITE)
                    }
                }
                bitmap
            }.onFailure {
                Log.e("QrEncoder", "Failed to encode QR: ${it.message}", it)
            }
        }
}
