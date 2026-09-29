package com.example.asma_ul_husna.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.asma_ul_husna.ui.theme.BrightGold
import com.example.asma_ul_husna.ui.theme.DarkGold
import com.example.asma_ul_husna.ui.theme.IslamicGold
import com.example.asma_ul_husna.ui.theme.PaleGold

/**
 * Distinct, deterministic animation entrance styles for the 99 Names of Allah.
 *
 * Each style defines a unique physical and spatial choreography (3D perspective tilt,
 * directional trajectory, scale curve, and depth bloom) to present each Name as a
 * cinematic floating centerpiece during full recitation.
 */
enum class NameAnimationStyle(val styleName: String, val description: String) {
    /** Style A: Enters gracefully from below with upward perspective, settling into hover. */
    FLOAT_RISE(
        styleName = "Float from Below",
        description = "Rises smoothly from lower depth with subtle upward tilt and scales into focus."
    ),

    /** Style B: Deep 3D perspective approach with dual-axis tilt (pitch & yaw). */
    DEPTH_3D_PERSPECTIVE(
        styleName = "3D Depth Perspective",
        description = "Approaches from spatial depth with combined X/Y tilt and straightens into center."
    ),

    /** Style C: Fluid diagonal glide from the top-right corner with gentle angular momentum. */
    DIAGONAL_GLIDE(
        styleName = "Diagonal Glide",
        description = "Enters along a diagonal vector with subtle roll rotation and eases into hover."
    ),

    /** Style D: Elegant 3D perspective unfold around the Y-axis. */
    FLIP_PERSPECTIVE_REVEAL(
        styleName = "Flip Perspective Reveal",
        description = "Rotates into view with a 3D perspective unfold (50° to 0°) and arrives in center."
    ),

    /** Style E: Cosmic zoom emerging from the deep background with radial luminescence. */
    COSMIC_ZOOM_BLOOM(
        styleName = "Cosmic Zoom & Bloom",
        description = "Emerges from far depth with scale expansion and counter-tilt settling."
    ),

    /** Style F: Sweeping arc trajectory with harmonious multi-axis easing. */
    ARC_SWEEP(
        styleName = "Arc Sweep & Settle",
        description = "Sweeps across a curved arc trajectory with counter-rotational harmony."
    ),

    /** Style G: Majestic ascension with radiant vertical elevation and luminous warmth. */
    RADIANT_ASCEND(
        styleName = "Radiant Ascension",
        description = "Ascends majestically along the vertical axis with a slight side-angle gaze."
    ),

    /** Style H: Symmetrical royal unfold with deep camera perspective. */
    MAJESTIC_UNFOLD(
        styleName = "Majestic Unfold",
        description = "Unfolds gracefully with multi-axis 3D angles settling into focal balance."
    )
}

/**
 * Geometric and spatial transformation values applied to the floating card graphics layer.
 */
data class PresentationTransform(
    val scale: Float = 1.0f,
    val translationX: Dp = 0.dp,
    val translationY: Dp = 0.dp,
    val rotationX: Float = 0.0f,
    val rotationY: Float = 0.0f,
    val rotationZ: Float = 0.0f,
    val alpha: Float = 1.0f,
    val glowIntensity: Float = 1.0f
)

/**
 * Deterministically maps a 1-based Name ID to its designated [NameAnimationStyle].
 * Ensures reproducible visual choreography across sessions and testing without randomness.
 */
fun getAnimationStyleForName(nameId: Int): NameAnimationStyle {
    val styles = NameAnimationStyle.values()
    val index = (nameId - 1).coerceAtLeast(0) % styles.size
    return styles[index]
}

/**
 * Computes the interpolated spatial transform for a given [NameAnimationStyle]
 * based on entrance progress (0.0f = start of entrance, 1.0f = fully arrived in center).
 */
fun calculateEntranceTransform(
    style: NameAnimationStyle,
    progress: Float,
    targetScale: Float = 1.08f
): PresentationTransform {
    val clampedProgress = progress.coerceIn(0.0f, 1.0f)
    // Apply smooth cubic easing to entrance kinematics
    val easedProgress = FastOutSlowInEasing.transform(clampedProgress)
    val inverse = 1.0f - easedProgress

    return when (style) {
        NameAnimationStyle.FLOAT_RISE -> {
            PresentationTransform(
                scale = lerpFloat(0.80f, targetScale, easedProgress),
                translationX = 0.dp,
                translationY = (inverse * 50f).dp,
                rotationX = inverse * -9.0f,
                rotationY = 0.0f,
                rotationZ = 0.0f,
                alpha = lerpFloat(0.15f, 1.0f, clampedProgress),
                glowIntensity = easedProgress
            )
        }

        NameAnimationStyle.DEPTH_3D_PERSPECTIVE -> {
            PresentationTransform(
                scale = lerpFloat(0.70f, targetScale, easedProgress),
                translationX = (inverse * -16f).dp,
                translationY = (inverse * 32f).dp,
                rotationX = inverse * 14.0f,
                rotationY = inverse * -15.0f,
                rotationZ = 0.0f,
                alpha = lerpFloat(0.10f, 1.0f, clampedProgress),
                glowIntensity = easedProgress
            )
        }

        NameAnimationStyle.DIAGONAL_GLIDE -> {
            PresentationTransform(
                scale = lerpFloat(0.82f, targetScale, easedProgress),
                translationX = (inverse * 38f).dp,
                translationY = (inverse * 36f).dp,
                rotationX = inverse * 6.0f,
                rotationY = 0.0f,
                rotationZ = inverse * 5.0f,
                alpha = lerpFloat(0.15f, 1.0f, clampedProgress),
                glowIntensity = easedProgress
            )
        }

        NameAnimationStyle.FLIP_PERSPECTIVE_REVEAL -> {
            PresentationTransform(
                scale = lerpFloat(0.84f, targetScale, easedProgress),
                translationX = 0.dp,
                translationY = (inverse * 20f).dp,
                rotationX = inverse * -4.0f,
                rotationY = inverse * 52.0f,
                rotationZ = 0.0f,
                alpha = lerpFloat(0.20f, 1.0f, clampedProgress),
                glowIntensity = easedProgress
            )
        }

        NameAnimationStyle.COSMIC_ZOOM_BLOOM -> {
            PresentationTransform(
                scale = lerpFloat(0.64f, targetScale, easedProgress),
                translationX = 0.dp,
                translationY = (inverse * 18f).dp,
                rotationX = 0.0f,
                rotationY = 0.0f,
                rotationZ = inverse * -4.0f,
                alpha = lerpFloat(0.05f, 1.0f, clampedProgress),
                glowIntensity = easedProgress
            )
        }

        NameAnimationStyle.ARC_SWEEP -> {
            PresentationTransform(
                scale = lerpFloat(0.80f, targetScale, easedProgress),
                translationX = (inverse * -36f).dp,
                translationY = (inverse * 32f).dp,
                rotationX = inverse * 5.0f,
                rotationY = inverse * 9.0f,
                rotationZ = inverse * -4.5f,
                alpha = lerpFloat(0.15f, 1.0f, clampedProgress),
                glowIntensity = easedProgress
            )
        }

        NameAnimationStyle.RADIANT_ASCEND -> {
            PresentationTransform(
                scale = lerpFloat(0.76f, targetScale, easedProgress),
                translationX = 0.dp,
                translationY = (inverse * 56f).dp,
                rotationX = inverse * -11.0f,
                rotationY = inverse * 8.0f,
                rotationZ = 0.0f,
                alpha = lerpFloat(0.10f, 1.0f, clampedProgress),
                glowIntensity = easedProgress
            )
        }

        NameAnimationStyle.MAJESTIC_UNFOLD -> {
            PresentationTransform(
                scale = lerpFloat(0.78f, targetScale, easedProgress),
                translationX = (inverse * 14f).dp,
                translationY = (inverse * -24f).dp,
                rotationX = inverse * 10.0f,
                rotationY = inverse * 13.0f,
                rotationZ = inverse * 3.0f,
                alpha = lerpFloat(0.12f, 1.0f, clampedProgress),
                glowIntensity = easedProgress
            )
        }
    }
}

/**
 * Creates a dynamic golden halo brush for the illuminated backdrop glow.
 */
fun createGoldGlowBrush(alphaMultiplier: Float): Brush {
    val clampedAlpha = alphaMultiplier.coerceIn(0.0f, 1.0f)
    return Brush.radialGradient(
        colors = listOf(
            BrightGold.copy(alpha = 0.35f * clampedAlpha),
            IslamicGold.copy(alpha = 0.20f * clampedAlpha),
            DarkGold.copy(alpha = 0.08f * clampedAlpha),
            Color.Transparent
        )
    )
}

/**
 * Creates an animated shimmering gold border brush for the active centerpiece card.
 */
fun createShimmerGoldBorder(shimmerOffset: Float): Brush {
    val normalizedOffset = shimmerOffset % 1.0f
    return Brush.linearGradient(
        colors = listOf(
            BrightGold,
            PaleGold,
            IslamicGold,
            BrightGold
        ),
        start = Offset(normalizedOffset * 400f, 0f),
        end = Offset((normalizedOffset + 1f) * 400f, 400f)
    )
}

private fun lerpFloat(start: Float, stop: Float, fraction: Float): Float {
    return start + (stop - start) * fraction.coerceIn(0.0f, 1.0f)
}
