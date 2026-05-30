package me.egil_accamacho.classtrack.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.egil_accamacho.classtrack.ui.theme.ClasstrackTheme
import me.egil_accamacho.classtrack.ui.theme.CtBorderLight
import me.egil_accamacho.classtrack.ui.theme.CtSpacing
import me.egil_accamacho.classtrack.ui.theme.ShapeSecondary

/**
 * Skeleton loading shimmer — animated left-to-right gradient sweep.
 * Per Manual de Marca §13: use skeleton loading for async content.
 *
 * Usage: call [SkeletonCourseCard], [SkeletonMetricCard], or compose your own
 * layout using [ShimmerBox].
 */

// ── Base shimmer box ──────────────────────────────────────────────────────────

/**
 * A single skeleton rectangle with a shimmering gradient animation.
 *
 * @param modifier   Controls size; caller must supply width/height
 */
@Composable
fun ShimmerBox(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "shimmerTranslate",
    )

    val shimmerColors = listOf(
        Color(0xFFE2E8F0),
        Color(0xFFF1F5F9),
        Color(0xFFE2E8F0),
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 300f, 0f),
        end = Offset(translateAnim, 0f),
    )

    Box(
        modifier = modifier
            .clip(ShapeSecondary)
            .background(brush),
    )
}

// ── Pre-built skeleton cards ──────────────────────────────────────────────────

/** Skeleton version of [CourseCard] shown while course list loads. */
@Composable
fun SkeletonCourseCard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(ShapeSecondary)
            .background(Color.White)
            .padding(17.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                ShimmerBox(modifier = Modifier.width(160.dp).height(20.dp))
                Spacer(modifier = Modifier.height(CtSpacing.sm))
                ShimmerBox(modifier = Modifier.width(100.dp).height(14.dp))
            }
            ShimmerBox(modifier = Modifier.width(48.dp).height(24.dp))
        }
        Spacer(modifier = Modifier.height(CtSpacing.md))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(CtBorderLight),
        )
        Spacer(modifier = Modifier.height(CtSpacing.md))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            ShimmerBox(modifier = Modifier.width(120.dp).height(14.dp))
            ShimmerBox(modifier = Modifier.width(70.dp).height(14.dp))
        }
    }
}

/** Skeleton version of [MetricCard] shown while dashboard metrics load. */
@Composable
fun SkeletonMetricCard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(ShapeSecondary)
            .background(Color.White)
            .padding(17.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        ShimmerBox(modifier = Modifier.fillMaxWidth(0.7f).height(12.dp))
        Spacer(modifier = Modifier.height(CtSpacing.md))
        ShimmerBox(modifier = Modifier.width(56.dp).height(28.dp))
    }
}

/** Full-screen skeleton for a list of course cards. */
@Composable
fun LoadingState(
    modifier: Modifier = Modifier,
    cardCount: Int = 3,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(CtSpacing.base),
        verticalArrangement = Arrangement.spacedBy(CtSpacing.md),
    ) {
        repeat(cardCount) {
            SkeletonCourseCard()
            Spacer(modifier = Modifier.height(0.dp))
        }
    }
}

// ── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC)
@Composable
private fun LoadingStatePreview() {
    ClasstrackTheme {
        LoadingState()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAFC)
@Composable
private fun SkeletonMetricCardPreview() {
    ClasstrackTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CtSpacing.base),
            horizontalArrangement = Arrangement.spacedBy(CtSpacing.md),
        ) {
            SkeletonMetricCard(modifier = Modifier.weight(1f).height(98.dp))
            SkeletonMetricCard(modifier = Modifier.weight(1f).height(98.dp))
        }
    }
}
