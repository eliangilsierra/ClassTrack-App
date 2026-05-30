package me.egil_accamacho.classtrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PersonRemove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import me.egil_accamacho.classtrack.ui.theme.CtPrimaryLight
import me.egil_accamacho.classtrack.ui.theme.CtSpacing
import me.egil_accamacho.classtrack.ui.theme.ShapeSecondary

/**
 * Student card for Course Detail / Link Students screens.
 * Shows an initials avatar, student name, email/ID, and an optional remove action.
 *
 * @param studentName   Full name of the student
 * @param email         Student's institutional email or identifier
 * @param modifier      Optional modifier
 * @param onRemove      When non-null, shows a remove icon button. Called on tap.
 * @param onClick       Called when the card body is tapped (optional)
 */
@Composable
fun StudentCard(
    studentName: String,
    email: String,
    modifier: Modifier = Modifier,
    onRemove: (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
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
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .padding(CtSpacing.base),
    ) {
        // ── Initials avatar ────────────────────────────────────────────────
        InitialsAvatar(name = studentName, size = 40)

        Spacer(modifier = Modifier.width(CtSpacing.md))

        // ── Name + email ───────────────────────────────────────────────────
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = studentName,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
            )
            Text(
                text = email,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
            )
        }

        // ── Remove button ──────────────────────────────────────────────────
        if (onRemove != null) {
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(36.dp),
            ) {
                Icon(
                    imageVector = Icons.Rounded.PersonRemove,
                    contentDescription = "Eliminar estudiante",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}

/** Small circular avatar generated from the first initials of [name]. */
@Composable
fun InitialsAvatar(
    name: String,
    size: Int,
    modifier: Modifier = Modifier,
) {
    val initials = name
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercaseChar().toString() }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(CtPrimaryLight.copy(alpha = 0.3f))
            .border(1.dp, CtPrimary.copy(alpha = 0.2f), CircleShape),
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = (size * 0.35f).toInt().coerceAtMost(14).let {
                    MaterialTheme.typography.labelLarge.fontSize
                },
            ),
            color = CtPrimary,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC)
@Composable
private fun StudentCardPreview() {
    ClasstrackTheme {
        Column(
            modifier = Modifier.padding(CtSpacing.base),
            verticalArrangement = Arrangement.spacedBy(CtSpacing.sm),
        ) {
            StudentCard(
                studentName = "Julian Rivera",
                email = "j.rivera@universidad.edu",
                onRemove = {},
            )
            StudentCard(
                studentName = "María García",
                email = "m.garcia@universidad.edu",
            )
        }
    }
}
