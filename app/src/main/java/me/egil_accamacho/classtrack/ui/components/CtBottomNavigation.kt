package me.egil_accamacho.classtrack.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.egil_accamacho.classtrack.ui.theme.ClasstrackTheme
import me.egil_accamacho.classtrack.ui.theme.CtSecondary
import me.egil_accamacho.classtrack.ui.theme.CtSpacing

/**
 * A single navigation item descriptor.
 *
 * @param route    Navigation route string (used for selection comparison)
 * @param icon     Material icon vector
 * @param label    Display label shown below the icon
 */
data class CtNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String,
)

/**
 * ClassTrack custom bottom navigation bar — height 80dp, bg #F7F9FB, rounded top corners 12dp.
 * Figma: "BottomNavBar (Mobile Only)" node in Home Teacher, Courses, Digital ID screens.
 *
 * Active tab shows a pill-shaped background in [CtSecondary] (#8B5CF6) with white text/icon.
 * Inactive tabs show icon+label in [onSurfaceVariant].
 *
 * Teacher has 3 tabs: Home → Courses → Profile.
 * Student has 2 tabs: Home → Profile.
 * Callers pass the appropriate [items] list.
 *
 * @param items         Navigation items to display
 * @param currentRoute  The currently selected route
 * @param onItemClick   Callback with the tapped route string
 */
@Composable
fun CtBottomNavigation(
    items: List<CtNavItem>,
    currentRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                clip = false,
            )
            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            .background(MaterialTheme.colorScheme.background),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = CtSpacing.lg),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEach { item ->
                val isSelected = item.route == currentRoute
                NavPill(
                    item = item,
                    isSelected = isSelected,
                    onClick = { onItemClick(item.route) },
                )
            }
        }
    }
}

@Composable
private fun NavPill(
    item: CtNavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val pillBg = if (isSelected) CtSecondary else Color.Transparent
    val contentColor = if (isSelected) {
        Color.White
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(pillBg)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = CtSpacing.lg, vertical = CtSpacing.xs),
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = contentColor,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = item.label,
            style = MaterialTheme.typography.labelMedium,
            color = contentColor,
        )
    }
}

// ── Convenience item lists ────────────────────────────────────────────────────

/** Bottom navigation items for the Teacher role (3 tabs). */
val teacherNavItems = listOf(
    CtNavItem(route = "home_teacher", icon = Icons.Rounded.Home, label = "Home"),
    CtNavItem(route = "courses", icon = Icons.Rounded.School, label = "Courses"),
    CtNavItem(route = "profile", icon = Icons.Rounded.Person, label = "Profile"),
)

/** Bottom navigation items for the Student role (2 tabs). */
val studentNavItems = listOf(
    CtNavItem(route = "home_student", icon = Icons.Rounded.Home, label = "Home"),
    CtNavItem(route = "profile", icon = Icons.Rounded.Person, label = "Profile"),
)

// ── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
private fun CtBottomNavigationTeacherPreview() {
    ClasstrackTheme {
        CtBottomNavigation(
            items = teacherNavItems,
            currentRoute = "home_teacher",
            onItemClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CtBottomNavigationStudentPreview() {
    ClasstrackTheme {
        CtBottomNavigation(
            items = studentNavItems,
            currentRoute = "profile",
            onItemClick = {},
        )
    }
}
