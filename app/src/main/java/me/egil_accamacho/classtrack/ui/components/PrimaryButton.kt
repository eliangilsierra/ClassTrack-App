package me.egil_accamacho.classtrack.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.egil_accamacho.classtrack.ui.theme.ClasstrackTheme
import me.egil_accamacho.classtrack.ui.theme.CtPrimary
import me.egil_accamacho.classtrack.ui.theme.CtSpacing
import me.egil_accamacho.classtrack.ui.theme.ShapePrimary

/**
 * Primary CTA button — filled purple #7C3AED, height 56dp, radius 16dp.
 * Figma: "Primary Button" nodes across Login, Register, Course, Attendance screens.
 *
 * @param text   Button label
 * @param onClick Click action
 * @param modifier Optional modifier; defaults to fillMaxWidth
 * @param enabled Whether the button is interactive
 * @param loading Shows a spinner and disables interaction when true
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = ShapePrimary,
        colors = ButtonDefaults.buttonColors(
            containerColor = CtPrimary,
            contentColor = Color.White,
            disabledContainerColor = CtPrimary.copy(alpha = 0.5f),
            disabledContentColor = Color.White.copy(alpha = 0.7f),
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 1.dp,
            pressedElevation = 2.dp,
            disabledElevation = 0.dp,
        ),
        contentPadding = PaddingValues(horizontal = CtSpacing.lg, vertical = 0.dp),
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White,
                strokeWidth = 2.dp,
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC)
@Composable
private fun PrimaryButtonPreview() {
    ClasstrackTheme {
        Column(
            modifier = Modifier.padding(CtSpacing.base),
            verticalArrangement = Arrangement.spacedBy(CtSpacing.sm),
        ) {
            PrimaryButton(text = "Iniciar Sesión", onClick = {})
            PrimaryButton(text = "Cargando...", onClick = {}, loading = true)
            PrimaryButton(text = "Deshabilitado", onClick = {}, enabled = false)
        }
    }
}
