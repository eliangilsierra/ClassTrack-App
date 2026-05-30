package me.egil_accamacho.classtrack.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Inbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.egil_accamacho.classtrack.ui.theme.ClasstrackTheme
import me.egil_accamacho.classtrack.ui.theme.CtPrimary
import me.egil_accamacho.classtrack.ui.theme.CtSpacing

/**
 * Generic empty state view — centered icon, title, subtitle, optional CTA.
 * Per spec DoD every list/content screen must handle the empty state.
 *
 * @param title       Primary message (e.g. "Sin cursos aún")
 * @param subtitle    Supporting explanation
 * @param icon        Material icon to display (defaults to inbox)
 * @param modifier    Optional modifier
 * @param action      Optional composable for a CTA button below the subtitle
 */
@Composable
fun EmptyState(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Rounded.Inbox,
    action: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = CtSpacing.xl, vertical = CtSpacing.xxl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = CtPrimary.copy(alpha = 0.4f),
            modifier = Modifier.size(72.dp),
        )

        Spacer(modifier = Modifier.height(CtSpacing.base))

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(CtSpacing.sm))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        if (action != null) {
            Spacer(modifier = Modifier.height(CtSpacing.lg))
            action()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC)
@Composable
private fun EmptyStatePreview() {
    ClasstrackTheme {
        EmptyState(
            title = "Sin cursos aún",
            subtitle = "Crea tu primer curso para comenzar a registrar asistencias.",
            action = {
                PrimaryButton(
                    text = "Crear Curso",
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(0.7f),
                )
            },
        )
    }
}
