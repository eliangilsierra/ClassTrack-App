package me.egil_accamacho.classtrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.egil_accamacho.classtrack.ui.theme.ClasstrackTheme
import me.egil_accamacho.classtrack.ui.theme.CtPrimary
import me.egil_accamacho.classtrack.ui.theme.CtSpacing

/**
 * ClassTrack top app bar — height 56dp, frosted-glass bg, title in primary color.
 * Figma: "Header - TopAppBar" nodes across Courses, Home, Courses, Digital ID screens.
 *
 * The bar renders with a semi-transparent white tint (alpha ~0.85) to simulate the
 * frosted glass effect seen in Figma (backdrop-blur on the web is not directly
 * available in Compose without custom rendering; the alpha approximation matches
 * the visual intent on light backgrounds).
 *
 * @param title      Screen title displayed in primary color (24sp Bold)
 * @param onBack     When non-null, a back arrow is shown as the leading icon
 * @param actions    Trailing icon buttons (right side)
 */
@Composable
fun CtTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(elevation = 1.dp)
            .background(
                color = MaterialTheme.colorScheme.background.copy(alpha = 0.92f),
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = CtSpacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Leading — back button or spacer
            if (onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(40.dp),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBackIosNew,
                        contentDescription = "Regresar",
                        tint = CtPrimary,
                        modifier = Modifier.size(20.dp),
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(CtSpacing.sm))
            }

            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                ),
                color = CtPrimary,
                maxLines = 1,
                modifier = Modifier.weight(1f),
            )

            // Trailing actions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                content = actions,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CtTopAppBarPreview() {
    ClasstrackTheme {
        CtTopAppBar(
            title = "Mis Cursos",
            onBack = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CtTopAppBarNoBackPreview() {
    ClasstrackTheme {
        CtTopAppBar(
            title = "Hola, Prof. Alex",
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBackIosNew,
                        contentDescription = "Notificaciones",
                        tint = Color.Gray,
                    )
                }
            },
        )
    }
}
