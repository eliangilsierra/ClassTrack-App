package me.egil_accamacho.classtrack.ui.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * ClassTrack shape system — Manual de Marca §7.
 *
 * Primary radius:   16dp — buttons, cards, text fields, dialogs
 * Secondary radius: 12dp — chips, secondary surfaces
 * FAB:              circular
 */
val CtShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small      = RoundedCornerShape(12.dp),   // secondary radius
    medium     = RoundedCornerShape(16.dp),   // primary radius — cards, dialogs
    large      = RoundedCornerShape(16.dp),   // primary radius — bottom sheets
    extraLarge = RoundedCornerShape(16.dp)
)

// Convenience aliases used in component code
val ShapePrimary   = RoundedCornerShape(16.dp)
val ShapeSecondary = RoundedCornerShape(12.dp)
val ShapeFab       = CircleShape
