package me.egil_accamacho.classtrack.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.egil_accamacho.classtrack.ui.theme.ClasstrackTheme
import me.egil_accamacho.classtrack.ui.theme.CtBorderLight
import me.egil_accamacho.classtrack.ui.theme.CtPrimary
import me.egil_accamacho.classtrack.ui.theme.CtSpacing
import me.egil_accamacho.classtrack.ui.theme.ShapePrimary

/**
 * Digital identification card showing user info + QR code.
 * Figma: "Article - Digital Wallet Card" in screen 17 / node 1:208.
 *
 * Structure:
 *   ┌─────────────────────────────────────────┐
 *   │ [gradient header]                       │
 *   │       ○ Avatar (80dp initials)          │
 *   │       Name (20sp SemiBold)              │
 *   │       [Role badge]                      │
 *   │       email (16sp Regular)              │
 *   ├─────────────────────────────────────────┤
 *   │ [bg #F7F9FB]                            │
 *   │       QrDisplay (240dp, corner marks)   │
 *   │       "Identificación Digital ClassTrack"│
 *   │       description text                  │
 *   └─────────────────────────────────────────┘
 *
 * @param fullName      Student/teacher full name
 * @param email         Institutional email
 * @param studentCode   Short identifier shown in the role badge (e.g. "ST-2024")
 * @param qrBitmap      Pre-rendered QR bitmap (null → loading spinner inside QrDisplay)
 * @param modifier      Optional modifier (typically fillMaxWidth)
 */
@Composable
fun DigitalIdCard(
    fullName: String,
    email: String,
    studentCode: String?,
    qrBitmap: Bitmap?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                shape = ShapePrimary,
                ambientColor = Color(0x1A0F172A),
                spotColor = Color(0x1A0F172A),
            )
            .clip(ShapePrimary)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, CtBorderLight, ShapePrimary),
    ) {
        // ── User info section ──────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            CtPrimary.copy(alpha = 0.10f),
                            Color.Transparent,
                        ),
                    ),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(
                    start = CtSpacing.lg,
                    end = CtSpacing.lg,
                    top = CtSpacing.lg,
                    bottom = CtSpacing.lg,
                ),
            ) {
                // Avatar
                InitialsAvatar(name = fullName, size = 80)

                Spacer(modifier = Modifier.height(CtSpacing.md))

                // Name
                Text(
                    text = fullName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Spacer(modifier = Modifier.height(CtSpacing.sm))

                // Role / code badge
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(CtPrimary.copy(alpha = 0.10f))
                        .border(1.dp, CtPrimary.copy(alpha = 0.20f), CircleShape)
                        .padding(horizontal = CtSpacing.md, vertical = CtSpacing.xs),
                ) {
                    Text(
                        text = studentCode ?: "—",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
                        color = CtPrimary,
                    )
                }

                Spacer(modifier = Modifier.height(CtSpacing.sm))

                // Email
                Text(
                    text = email,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        HorizontalDivider(color = CtBorderLight)

        // ── QR section ────────────────────────────────────────────────────
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(CtSpacing.lg),
            verticalArrangement = Arrangement.Center,
        ) {
            QrDisplay(
                bitmap = qrBitmap,
                modifier = Modifier.size(240.dp),
            )

            Spacer(modifier = Modifier.height(CtSpacing.lg))

            Text(
                text = "Identificación Digital ClassTrack",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(CtSpacing.sm))

            Text(
                text = "Este código puede ser escaneado por tus docentes para asociarte a cursos",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        }
    }
}

@Composable
private fun InitialsAvatar(name: String, size: Int) {
    val initials = name.trim().split("\\s+".toRegex())
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")
        .ifEmpty { "?" }

    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(CtPrimary),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = (size / 3).sp,
            ),
            color = Color.White,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC)
@Composable
private fun DigitalIdCardPreview() {
    ClasstrackTheme {
        DigitalIdCard(
            fullName = "Julian Rivera",
            email = "j.rivera@universidad.edu",
            studentCode = "ST-2024",
            qrBitmap = null,
            modifier = Modifier.padding(CtSpacing.base),
        )
    }
}
