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
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.People
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Timer
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
import me.egil_accamacho.classtrack.ui.theme.CtPrimary
import me.egil_accamacho.classtrack.ui.theme.CtSpacing
import me.egil_accamacho.classtrack.ui.theme.CtSuccess
import me.egil_accamacho.classtrack.ui.theme.CtWarning
import me.egil_accamacho.classtrack.ui.theme.ShapeSecondary

/** Status of an attendance session. */
enum class AttendanceSessionStatus { ACTIVE, CLOSED }

/**
 * Attendance session card — shows session title, date/time, student count, status badge.
 * Used in HomeTeacher quick actions and Reports screens.
 *
 * @param sessionTitle    E.g. course name or "Sesión #3"
 * @param dateTime        Human-readable date/time string (e.g. "Hoy, 10:30 AM")
 * @param checkedInCount  Number of students that have checked in
 * @param totalStudents   Total students enrolled in the course
 * @param status          ACTIVE or CLOSED
 * @param onClick         Called when the card is tapped
 * @param modifier        Optional modifier
 */
@Composable
fun AttendanceCard(
    sessionTitle: String,
    dateTime: String,
    checkedInCount: Int,
    totalStudents: Int,
    status: AttendanceSessionStatus,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
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
            .padding(CtSpacing.base),
    ) {
        // ── Header ─────────────────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = sessionTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Schedule,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(12.dp),
                    )
                    Spacer(modifier = Modifier.width(CtSpacing.xs))
                    Text(
                        text = dateTime,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            StatusBadge(status = status)
        }

        Spacer(modifier = Modifier.height(CtSpacing.md))
        HorizontalDivider(color = CtBorderLight)
        Spacer(modifier = Modifier.height(CtSpacing.md))

        // ── Footer: check-in count ─────────────────────────────────────────
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Rounded.People,
                contentDescription = null,
                tint = CtPrimary,
                modifier = Modifier.size(14.dp),
            )
            Spacer(modifier = Modifier.width(CtSpacing.xs))
            Text(
                text = "$checkedInCount / $totalStudents presentes",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Composable
private fun StatusBadge(status: AttendanceSessionStatus) {
    val (label, icon, color, bg) = when (status) {
        AttendanceSessionStatus.ACTIVE -> Quadruple(
            "Activa",
            Icons.Rounded.Timer,
            CtSuccess,
            CtSuccess.copy(alpha = 0.12f),
        )
        AttendanceSessionStatus.CLOSED -> Quadruple(
            "Cerrada",
            Icons.Rounded.CheckCircle,
            CtWarning,
            CtWarning.copy(alpha = 0.12f),
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .padding(horizontal = CtSpacing.sm, vertical = CtSpacing.xs),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(12.dp),
        )
        Spacer(modifier = Modifier.width(CtSpacing.xs))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            color = color,
        )
    }
}

/** Simple data class to enable Kotlin's destructuring for 4 values. */
private data class Quadruple<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)

private operator fun <A, B, C, D> Quadruple<A, B, C, D>.component1() = a
private operator fun <A, B, C, D> Quadruple<A, B, C, D>.component2() = b
private operator fun <A, B, C, D> Quadruple<A, B, C, D>.component3() = c
private operator fun <A, B, C, D> Quadruple<A, B, C, D>.component4() = d

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC)
@Composable
private fun AttendanceCardPreview() {
    ClasstrackTheme {
        Column(
            modifier = Modifier.padding(CtSpacing.base),
            verticalArrangement = Arrangement.spacedBy(CtSpacing.sm),
        ) {
            AttendanceCard(
                sessionTitle = "Cálculo Diferencial",
                dateTime = "Hoy, 10:30 AM",
                checkedInCount = 35,
                totalStudents = 42,
                status = AttendanceSessionStatus.ACTIVE,
                onClick = {},
            )
            AttendanceCard(
                sessionTitle = "Álgebra Lineal",
                dateTime = "Lunes, 08:00 AM",
                checkedInCount = 30,
                totalStudents = 38,
                status = AttendanceSessionStatus.CLOSED,
                onClick = {},
            )
        }
    }
}
