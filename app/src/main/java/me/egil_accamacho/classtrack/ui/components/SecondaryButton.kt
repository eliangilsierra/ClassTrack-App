package me.egil_accamacho.classtrack.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.egil_accamacho.classtrack.ui.theme.ClasstrackTheme
import me.egil_accamacho.classtrack.ui.theme.CtPrimary
import me.egil_accamacho.classtrack.ui.theme.CtSpacing
import me.egil_accamacho.classtrack.ui.theme.ShapePrimary

/**
 * Secondary outlined button — border #7C3AED, height 56dp, radius 16dp.
 * Figma: "Button" outlined variant on Digital ID screen + misc secondary actions.
 *
 * @param text    Button label
 * @param onClick Click action
 * @param modifier Optional modifier; defaults to fillMaxWidth
 * @param enabled Whether the button is interactive
 * @param loading Shows a spinner and disables interaction when true
 */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled && !loading,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = ShapePrimary,
        border = BorderStroke(
            width = 1.dp,
            color = if (enabled && !loading) CtPrimary else CtPrimary.copy(alpha = 0.4f),
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = CtPrimary,
            disabledContentColor = CtPrimary.copy(alpha = 0.4f),
        ),
        contentPadding = PaddingValues(horizontal = CtSpacing.lg, vertical = 0.dp),
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = CtPrimary,
                strokeWidth = 2.dp,
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC)
@Composable
private fun SecondaryButtonPreview() {
    ClasstrackTheme {
        Column(
            modifier = Modifier.padding(CtSpacing.base),
            verticalArrangement = Arrangement.spacedBy(CtSpacing.sm),
        ) {
            SecondaryButton(text = "Compartir QR", onClick = {})
            SecondaryButton(text = "Cargando...", onClick = {}, loading = true)
            SecondaryButton(text = "Deshabilitado", onClick = {}, enabled = false)
        }
    }
}
