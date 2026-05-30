package me.egil_accamacho.classtrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.egil_accamacho.classtrack.ui.theme.ClasstrackTheme
import me.egil_accamacho.classtrack.ui.theme.CtBorderLight
import me.egil_accamacho.classtrack.ui.theme.CtSpacing
import me.egil_accamacho.classtrack.ui.theme.CtSuccess
import me.egil_accamacho.classtrack.ui.theme.ShapeSecondary

/**
 * Stripe-inspired metric card — white, border #E2E8F0, radius 12dp, padding 17dp.
 * Figma: "Metric Card 1–4" in Home Teacher grid.
 *
 * Layout (top to bottom):
 *   [Icon] [Label 12sp Medium secondary text]
 *   [Value 28sp Bold] (+ optional [Trend] small text)
 *
 * @param label       Metric label (e.g. "Cursos Activos")
 * @param value       Primary metric value (e.g. "12" or "88%")
 * @param icon        Material icon for the label row
 * @param modifier    Optional modifier
 * @param trend       Optional trend string shown in [trendColor] next to value (e.g. "+2%")
 * @param trendColor  Color for the trend label (default: CtSuccess green)
 */
@Composable
fun MetricCard(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    trend: String? = null,
    trendColor: Color = CtSuccess,
) {
    Column(
        modifier = modifier
            .shadow(
                elevation = 2.dp,
                shape = ShapeSecondary,
                ambientColor = Color(0x0D0F172A),
                spotColor = Color(0x0D0F172A),
            )
            .clip(ShapeSecondary)
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = CtBorderLight,
                shape = ShapeSecondary,
            )
            .padding(17.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        // Top row: icon + label
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(14.dp),
            )
            Spacer(modifier = Modifier.width(CtSpacing.xs))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
            )
        }

        Spacer(modifier = Modifier.height(CtSpacing.md))

        // Bottom row: value + optional trend
        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 36.sp,
                ),
                color = MaterialTheme.colorScheme.onBackground,
            )
            if (trend != null) {
                Spacer(modifier = Modifier.width(CtSpacing.sm))
                Text(
                    text = trend,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                    color = trendColor,
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC)
@Composable
private fun MetricCardPreview() {
    ClasstrackTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CtSpacing.base),
            horizontalArrangement = Arrangement.spacedBy(CtSpacing.md),
        ) {
            MetricCard(
                label = "Cursos Activos",
                value = "12",
                icon = Icons.Rounded.School,
                modifier = Modifier.weight(1f),
            )
            MetricCard(
                label = "Asistencia Promedio",
                value = "88%",
                icon = Icons.Rounded.TrendingUp,
                trend = "+2%",
                modifier = Modifier.weight(1f),
            )
        }
    }
}
