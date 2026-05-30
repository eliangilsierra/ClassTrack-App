package me.egil_accamacho.classtrack.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// ── Color schemes ─────────────────────────────────────────────────────────────
// Dynamic color is intentionally DISABLED to preserve the ClassTrack brand.
// See docs/Manual_Marca_ClassTrack.md §8.

private val CtLightColorScheme = lightColorScheme(
    primary          = CtPrimary,
    onPrimary        = CtOnPrimary,
    primaryContainer = CtPrimaryLight,
    secondary        = CtSecondary,
    onSecondary      = CtOnPrimary,
    background       = CtBackgroundLight,
    onBackground     = CtPrimaryTextLight,
    surface          = CtSurfaceLight,
    onSurface        = CtPrimaryTextLight,
    surfaceVariant   = CtSurfaceVariantLight,
    onSurfaceVariant = CtSecondaryTextLight,
    error            = CtError,
    onError          = CtOnPrimary,
    outline          = CtBorderLight,
)

private val CtDarkColorScheme = darkColorScheme(
    primary          = CtPrimary,
    onPrimary        = CtOnPrimary,
    primaryContainer = CtPrimaryDark,
    secondary        = CtSecondary,
    onSecondary      = CtOnPrimary,
    background       = CtBackgroundDark,
    onBackground     = CtPrimaryTextDark,
    surface          = CtSurfaceDark,
    onSurface        = CtPrimaryTextDark,
    surfaceVariant   = CtSurfaceVariantDark,
    onSurfaceVariant = CtSecondaryTextDark,
    error            = CtError,
    onError          = CtOnPrimary,
)

// ── Theme entry point ─────────────────────────────────────────────────────────

@Composable
fun ClasstrackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) CtDarkColorScheme else CtLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        shapes      = CtShapes,
        content     = content
    )
}
