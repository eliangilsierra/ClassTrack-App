package me.egil_accamacho.classtrack.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import me.egil_accamacho.classtrack.ui.theme.ClasstrackTheme
import me.egil_accamacho.classtrack.ui.theme.CtBorderLight
import me.egil_accamacho.classtrack.ui.theme.CtPrimary
import me.egil_accamacho.classtrack.ui.theme.CtSpacing
import me.egil_accamacho.classtrack.ui.theme.CtSuccess
import me.egil_accamacho.classtrack.ui.theme.CtWarning

/**
 * Displays a QR code bitmap inside a white card with purple corner markers.
 * Figma: "QR Code Container" in Digital ID screen (node 1:208).
 *
 * Optionally shows a countdown timer for attendance session QRs with
 * validity indicator (green → amber → red as time runs out).
 *
 * @param bitmap          The QR code bitmap to display. Null shows a loading spinner.
 * @param modifier        Optional modifier
 * @param remainingSeconds When > 0, shows a countdown + validity indicator below the QR.
 *                         When null, the timer section is hidden (for permanent user QRs).
 * @param totalSeconds    Total duration for progress calculation (default: 300 = 5 min)
 * @param onExpired       Called when the countdown reaches 0
 */
@Composable
fun QrDisplay(
    bitmap: Bitmap?,
    modifier: Modifier = Modifier,
    remainingSeconds: Int? = null,
    totalSeconds: Int = 300,
    onExpired: (() -> Unit)? = null,
) {
    // Live countdown ticker — decrements each second from the initial remainingSeconds
    var seconds by remember(remainingSeconds) {
        mutableIntStateOf(remainingSeconds ?: 0)
    }

    if (remainingSeconds != null && remainingSeconds > 0) {
        LaunchedEffect(remainingSeconds) {
            while (seconds > 0) {
                delay(1000L)
                seconds--
            }
            onExpired?.invoke()
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // ── QR Container ───────────────────────────────────────────────────
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .border(1.dp, CtBorderLight, RoundedCornerShape(12.dp))
                .padding(13.dp)
                .drawWithContent {
                    drawContent()
                    drawCornerMarkers(
                        color = CtPrimary,
                        cornerSize = 16.dp.toPx(),
                        strokeWidth = 2.dp.toPx(),
                        cornerRadius = 8.dp.toPx(),
                    )
                },
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Código QR",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                )
            } else {
                CircularProgressIndicator(
                    color = CtPrimary,
                    modifier = Modifier.size(40.dp),
                )
            }
        }

        // ── Countdown + validity ───────────────────────────────────────────
        if (remainingSeconds != null) {
            Spacer(modifier = Modifier.height(CtSpacing.md))
            val ratio = if (totalSeconds > 0) seconds.toFloat() / totalSeconds else 0f
            val validityColor = when {
                ratio > 0.5f -> CtSuccess
                ratio > 0.2f -> CtWarning
                else         -> MaterialTheme.colorScheme.error
            }
            Text(
                text = formatCountdown(seconds),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                ),
                color = validityColor,
            )
            Text(
                text = if (seconds > 0) "QR válido" else "QR expirado",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/** Draws the 4 corner "L" markers on top of the QR container. */
private fun DrawScope.drawCornerMarkers(
    color: Color,
    cornerSize: Float,
    strokeWidth: Float,
    cornerRadius: Float,
) {
    val stroke = Stroke(width = strokeWidth)
    val w = size.width
    val h = size.height
    val r = cornerRadius

    // Top-left
    drawArc(color, startAngle = 180f, sweepAngle = 90f, useCenter = false,
        topLeft = Offset(0f, 0f), size = Size(r * 2, r * 2), style = stroke)
    drawLine(color, Offset(r, strokeWidth / 2), Offset(cornerSize, strokeWidth / 2), strokeWidth)
    drawLine(color, Offset(strokeWidth / 2, r), Offset(strokeWidth / 2, cornerSize), strokeWidth)

    // Top-right
    drawArc(color, startAngle = 270f, sweepAngle = 90f, useCenter = false,
        topLeft = Offset(w - r * 2, 0f), size = Size(r * 2, r * 2), style = stroke)
    drawLine(color, Offset(w - cornerSize, strokeWidth / 2), Offset(w - r, strokeWidth / 2), strokeWidth)
    drawLine(color, Offset(w - strokeWidth / 2, r), Offset(w - strokeWidth / 2, cornerSize), strokeWidth)

    // Bottom-left
    drawArc(color, startAngle = 90f, sweepAngle = 90f, useCenter = false,
        topLeft = Offset(0f, h - r * 2), size = Size(r * 2, r * 2), style = stroke)
    drawLine(color, Offset(r, h - strokeWidth / 2), Offset(cornerSize, h - strokeWidth / 2), strokeWidth)
    drawLine(color, Offset(strokeWidth / 2, h - cornerSize), Offset(strokeWidth / 2, h - r), strokeWidth)

    // Bottom-right
    drawArc(color, startAngle = 0f, sweepAngle = 90f, useCenter = false,
        topLeft = Offset(w - r * 2, h - r * 2), size = Size(r * 2, r * 2), style = stroke)
    drawLine(color, Offset(w - cornerSize, h - strokeWidth / 2), Offset(w - r, h - strokeWidth / 2), strokeWidth)
    drawLine(color, Offset(w - strokeWidth / 2, h - cornerSize), Offset(w - strokeWidth / 2, h - r), strokeWidth)
}

private fun formatCountdown(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%d:%02d".format(m, s)
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC)
@Composable
private fun QrDisplayPreview() {
    ClasstrackTheme {
        Column(
            modifier = Modifier.padding(CtSpacing.xl),
            verticalArrangement = Arrangement.spacedBy(CtSpacing.lg),
        ) {
            // No bitmap — loading state
            QrDisplay(
                bitmap = null,
                modifier = Modifier.size(240.dp),
            )
            // With countdown
            QrDisplay(
                bitmap = null,
                modifier = Modifier.size(240.dp),
                remainingSeconds = 180,
                totalSeconds = 300,
            )
        }
    }
}
