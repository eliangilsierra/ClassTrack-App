package me.egil_accamacho.classtrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.TrendingDown
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.IconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.egil_accamacho.classtrack.ui.theme.ClasstrackTheme
import me.egil_accamacho.classtrack.ui.theme.CtBorderLight
import me.egil_accamacho.classtrack.ui.theme.CtError
import me.egil_accamacho.classtrack.ui.theme.CtPrimary
import me.egil_accamacho.classtrack.ui.theme.CtSpacing
import me.egil_accamacho.classtrack.ui.theme.CtSuccess
import me.egil_accamacho.classtrack.ui.theme.CtWarning
import me.egil_accamacho.classtrack.ui.theme.ShapeSecondary

/**
 * Course card showing name, student count, attendance badge, and last session.
 * Figma: "Article - Card 1/2/3" on Lista de Cursos screen (node 1:270).
 *
 * Attendance badge color:
 *   - Green  (#10B981 bg 10%) when ≥ 80 %
 *   - Amber  (#F59E0B bg 10%) when 60–79 %
 *   - Red    (error   bg 10%) when < 60 %
 *
 * @param courseName      Name of the course
 * @param studentCount    Number of enrolled students
 * @param attendancePct   Attendance percentage (0–100), null to hide badge
 * @param lastSession     Human-readable last session string (e.g. "Ayer", "Lunes")
 * @param onClick         Called when the user taps the card or "Gestionar →"
 * @param modifier        Optional modifier
 */
@Composable
fun CourseCard(
    courseName: String,
    studentCount: Int,
    lastSession: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    attendancePct: Int? = null,
    onDelete: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = ShapeSecondary,
                ambientColor = Color(0x0D0F172A),
                spotColor = Color(0x0D0F172A),
            )
            .clip(ShapeSecondary)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, CtBorderLight, ShapeSecondary)
            .clickable(onClick = onClick)
            .padding(17.dp),
    ) {
        // ── Header row ─────────────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = courseName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Group,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp),
                    )
                    Spacer(modifier = Modifier.width(CtSpacing.xs))
                    Text(
                        text = "$studentCount Estudiantes",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            if (attendancePct != null) {
                AttendanceBadge(percentage = attendancePct)
            }
            if (onDelete != null) {
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Eliminar curso",
                        tint = CtError,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        }

        // ── Divider + footer ───────────────────────────────────────────────
        Spacer(modifier = Modifier.height(CtSpacing.md))
        HorizontalDivider(color = CtBorderLight)
        Spacer(modifier = Modifier.height(CtSpacing.md))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Última sesión: $lastSession",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable(onClick = onClick),
            ) {
                Text(
                    text = "Gestionar",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
                    color = CtPrimary,
                )
                Icon(
                    imageVector = Icons.Rounded.ChevronRight,
                    contentDescription = null,
                    tint = CtPrimary,
                    modifier = Modifier.size(14.dp),
                )
            }
        }
    }
}

/** Small pill badge showing the attendance percentage with colour-coded feedback. */
@Composable
private fun AttendanceBadge(percentage: Int, modifier: Modifier = Modifier) {
    val (badgeColor, bgColor, icon) = when {
        percentage >= 80 -> Triple(CtSuccess, CtSuccess.copy(alpha = 0.10f), Icons.Rounded.TrendingUp)
        percentage >= 60 -> Triple(CtWarning, CtWarning.copy(alpha = 0.10f), Icons.Rounded.TrendingDown)
        else             -> Triple(MaterialTheme.colorScheme.error, MaterialTheme.colorScheme.error.copy(alpha = 0.10f), Icons.Rounded.TrendingDown)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(bgColor)
            .padding(horizontal = CtSpacing.sm, vertical = CtSpacing.xs),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = badgeColor,
            modifier = Modifier.size(12.dp),
        )
        Spacer(modifier = Modifier.width(CtSpacing.xs))
        Text(
            text = "$percentage%",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
            color = badgeColor,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC)
@Composable
private fun CourseCardPreview() {
    ClasstrackTheme {
        Column(
            modifier = Modifier.padding(CtSpacing.base),
            verticalArrangement = Arrangement.spacedBy(CtSpacing.md),
        ) {
            CourseCard(
                courseName = "Cálculo Diferencial",
                studentCount = 42,
                attendancePct = 85,
                lastSession = "Ayer",
                onClick = {},
            )
            CourseCard(
                courseName = "Álgebra Lineal",
                studentCount = 38,
                attendancePct = 72,
                lastSession = "Lunes",
                onClick = {},
            )
            CourseCard(
                courseName = "Física Mecánica",
                studentCount = 50,
                attendancePct = 91,
                lastSession = "Hace 2 horas",
                onClick = {},
            )
        }
    }
}
