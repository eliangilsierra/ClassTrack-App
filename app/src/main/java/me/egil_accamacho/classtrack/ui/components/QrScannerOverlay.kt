package me.egil_accamacho.classtrack.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.egil_accamacho.classtrack.ui.theme.ClasstrackTheme
import me.egil_accamacho.classtrack.ui.theme.CtPrimary
import me.egil_accamacho.classtrack.ui.theme.CtSpacing

/**
 * Camera scanner overlay — dark translucent mask with a transparent square cutout,
 * purple corner markers, instruction text below the frame, and a dismiss button.
 *
 * Figma: "12. Escanear QR (Estudiante)" (node 1:2) and
 *        "9.1 Escanear QR de Estudiante" (node 1:851).
 *
 * Place this on top of a [CameraPreview] (Phase 7 CameraX composable):
 * ```
 * Box {
 *     CameraPreview(analyzer = ...)
 *     QrScannerOverlay(instruction = "Escanea el QR del estudiante", onDismiss = { ... })
 * }
 * ```
 *
 * @param instruction   Text shown below the scan frame
 * @param modifier      Optional modifier
 * @param cutoutSize    Side length of the transparent square (default 240dp)
 * @param onDismiss     Called when the user taps the close button (top-right)
 */
@Composable
fun QrScannerOverlay(
    instruction: String,
    modifier: Modifier = Modifier,
    cutoutSize: Dp = 240.dp,
    onDismiss: (() -> Unit)? = null,
) {
    Box(modifier = modifier.fillMaxSize()) {
        // ── Dimmed canvas with transparent cutout ──────────────────────────
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cutoutPx = cutoutSize.toPx()
            val left  = (size.width  - cutoutPx) / 2f
            val top   = (size.height - cutoutPx) / 2f

            val cutoutPath = Path().apply {
                addRoundRect(
                    RoundRect(
                        rect = Rect(Offset(left, top), Size(cutoutPx, cutoutPx)),
                        cornerRadius = CornerRadius(16.dp.toPx()),
                    ),
                )
            }

            // Overlay
            clipPath(cutoutPath, clipOp = ClipOp.Difference) {
                drawRect(color = Color.Black.copy(alpha = 0.65f))
            }

            // Corner markers
            drawScannerCorners(
                left = left,
                top = top,
                size = cutoutPx,
                markerColor = CtPrimary,
                markerLength = 24.dp.toPx(),
                strokeWidth = 3.dp.toPx(),
                cornerRadius = 8.dp.toPx(),
            )
        }

        // ── Instruction text below the cutout ──────────────────────────────
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = cutoutSize + 32.dp),
            ) {
                Text(
                    text = instruction,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = CtSpacing.xl),
                )
            }
        }

        // ── Close button ───────────────────────────────────────────────────
        if (onDismiss != null) {
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(CtSpacing.sm),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Cancelar escaneo",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}

/** Draws the "L"-shaped corner markers inside the scanner cutout area. */
private fun DrawScope.drawScannerCorners(
    left: Float,
    top: Float,
    size: Float,
    markerColor: Color,
    markerLength: Float,
    strokeWidth: Float,
    cornerRadius: Float,
) {
    val stroke = Stroke(width = strokeWidth)
    val right  = left + size
    val bottom = top  + size
    val r      = cornerRadius

    // Top-left
    drawLine(markerColor, Offset(left + r, top), Offset(left + markerLength, top), strokeWidth)
    drawLine(markerColor, Offset(left, top + r), Offset(left, top + markerLength), strokeWidth)
    drawArc(markerColor, 180f, 90f, false,
        Offset(left, top), Size(r * 2, r * 2), style = stroke)

    // Top-right
    drawLine(markerColor, Offset(right - markerLength, top), Offset(right - r, top), strokeWidth)
    drawLine(markerColor, Offset(right, top + r), Offset(right, top + markerLength), strokeWidth)
    drawArc(markerColor, 270f, 90f, false,
        Offset(right - r * 2, top), Size(r * 2, r * 2), style = stroke)

    // Bottom-left
    drawLine(markerColor, Offset(left + r, bottom), Offset(left + markerLength, bottom), strokeWidth)
    drawLine(markerColor, Offset(left, bottom - markerLength), Offset(left, bottom - r), strokeWidth)
    drawArc(markerColor, 90f, 90f, false,
        Offset(left, bottom - r * 2), Size(r * 2, r * 2), style = stroke)

    // Bottom-right
    drawLine(markerColor, Offset(right - markerLength, bottom), Offset(right - r, bottom), strokeWidth)
    drawLine(markerColor, Offset(right, bottom - markerLength), Offset(right, bottom - r), strokeWidth)
    drawArc(markerColor, 0f, 90f, false,
        Offset(right - r * 2, bottom - r * 2), Size(r * 2, r * 2), style = stroke)
}

@Preview(showBackground = false, backgroundColor = 0xFF000000)
@Composable
private fun QrScannerOverlayPreview() {
    ClasstrackTheme {
        Box(
            modifier = Modifier
                .size(390.dp, 844.dp),
        ) {
            QrScannerOverlay(
                instruction = "Escanea el QR del estudiante para vincularlo al curso",
                onDismiss = {},
            )
        }
    }
}
