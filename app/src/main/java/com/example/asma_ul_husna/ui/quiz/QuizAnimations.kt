package com.example.asma_ul_husna.ui.quiz

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

// Vibrant Palette for Quiz Animations
val QuizPartyPink = Color(0xFFFF2E93)
val QuizPartyCyan = Color(0xFF00F2FE)
val QuizPartyPurple = Color(0xFF9D4EDD)
val QuizPartyGold = Color(0xFFFFD166)
val QuizPartyGreen = Color(0xFF06D6A0)
val QuizPartyCoral = Color(0xFFFF5757)
val QuizPartyBlue = Color(0xFF4361EE)
val QuizPartyOrange = Color(0xFFFF9E00)

val ConfettiColors = listOf(
    QuizPartyPink,
    QuizPartyCyan,
    QuizPartyPurple,
    QuizPartyGold,
    QuizPartyGreen,
    QuizPartyCoral,
    QuizPartyBlue,
    QuizPartyOrange
)

enum class ParticleShape {
    CIRCLE,
    RECTANGLE,
    STAR,
    DIAMOND
}

private data class ConfettiParticle(
    val xInitial: Float,
    val yInitial: Float,
    val vx: Float,
    val vy: Float,
    val color: Color,
    val size: Float,
    val rotationSpeed: Float,
    val shape: ParticleShape,
    val lifeSpanMs: Long,
    val startTimeMs: Long,
    val wobbleSpeed: Float
)

/**
 * Continuous, high-performance Canvas-based Confetti & Sparkle particle animation.
 * Runs smoothly on a frame loop and stops cleanly when leaving composition.
 */
@Composable
fun ContinuousConfettiCelebration(
    modifier: Modifier = Modifier,
    particleCount: Int = 45
) {
    var frameTimeNanos by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        val startNanos = withFrameNanos { it }
        while (true) {
            frameTimeNanos = withFrameNanos { it } - startNanos
        }
    }

    val random = remember { Random(System.currentTimeMillis()) }
    val particles = remember {
        List(particleCount) { index ->
            createRandomParticle(random, staggerMs = (index * 120L) % 3500L)
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        if (width <= 0f || height <= 0f) return@Canvas

        val currentTimeMs = frameTimeNanos / 1_000_000L

        particles.forEachIndexed { i, particle ->
            val elapsed = (currentTimeMs + particle.startTimeMs) % particle.lifeSpanMs
            val progress = elapsed.toFloat() / particle.lifeSpanMs

            // Physics calculation
            val wobble = sin(elapsed * particle.wobbleSpeed * 0.005f) * 45f
            val x = (particle.xInitial * width + particle.vx * elapsed * 0.12f + wobble) % width
            val adjustedX = if (x < 0) x + width else x
            val y = (particle.yInitial * height + particle.vy * elapsed * 0.25f + 0.5f * 0.0004f * elapsed * elapsed) % height

            val alpha = when {
                progress < 0.1f -> progress / 0.1f
                progress > 0.85f -> (1f - progress) / 0.15f
                else -> 1f
            }.coerceIn(0f, 1f)

            val rotation = (elapsed * particle.rotationSpeed * 0.15f) % 360f

            rotate(degrees = rotation, pivot = Offset(adjustedX, y)) {
                when (particle.shape) {
                    ParticleShape.CIRCLE -> {
                        drawCircle(
                            color = particle.color.copy(alpha = alpha),
                            radius = particle.size * 0.5f,
                            center = Offset(adjustedX, y)
                        )
                    }

                    ParticleShape.RECTANGLE -> {
                        drawRoundRect(
                            color = particle.color.copy(alpha = alpha),
                            topLeft = Offset(adjustedX - particle.size * 0.6f, y - particle.size * 0.3f),
                            size = Size(particle.size * 1.2f, particle.size * 0.6f),
                            cornerRadius = CornerRadius(2f, 2f)
                        )
                    }

                    ParticleShape.DIAMOND -> {
                        val path = Path().apply {
                            moveTo(adjustedX, y - particle.size * 0.6f)
                            lineTo(adjustedX + particle.size * 0.5f, y)
                            lineTo(adjustedX, y + particle.size * 0.6f)
                            lineTo(adjustedX - particle.size * 0.5f, y)
                            close()
                        }
                        drawPath(path, particle.color.copy(alpha = alpha))
                    }

                    ParticleShape.STAR -> {
                        drawStarParticle(adjustedX, y, particle.size * 0.6f, particle.color.copy(alpha = alpha))
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawStarParticle(cx: Float, cy: Float, radius: Float, color: Color) {
    val path = Path()
    val points = 5
    val innerRadius = radius * 0.45f
    var angle = -Math.PI / 2.0

    for (i in 0 until points * 2) {
        val r = if (i % 2 == 0) radius else innerRadius
        val x = cx + (r * cos(angle)).toFloat()
        val y = cy + (r * sin(angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        angle += Math.PI / points
    }
    path.close()
    drawPath(path, color)
}

private fun createRandomParticle(random: Random, staggerMs: Long): ConfettiParticle {
    val colors = ConfettiColors
    val shapes = ParticleShape.values()
    return ConfettiParticle(
        xInitial = random.nextFloat(),
        yInitial = random.nextFloat() * -0.2f,
        vx = (random.nextFloat() - 0.5f) * 1.2f,
        vy = random.nextFloat() * 0.8f + 0.6f,
        color = colors[random.nextInt(colors.size)],
        size = random.nextFloat() * 12f + 10f,
        rotationSpeed = (random.nextFloat() - 0.5f) * 3f + 1f,
        shape = shapes[random.nextInt(shapes.size)],
        lifeSpanMs = random.nextLong(3000L, 5000L),
        startTimeMs = staggerMs,
        wobbleSpeed = random.nextFloat() * 1.5f + 0.8f
    )
}

/**
 * Cute, animated crying sad-face with continuous falling tears.
 * Displayed when score <= 40%.
 */
@Composable
fun PlayfulCryingFace(
    modifier: Modifier = Modifier,
    sizeDp: Int = 110
) {
    val infiniteTransition = rememberInfiniteTransition(label = "CryingFaceTransition")

    // Gentle head wobble
    val headOffsetYSin by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "HeadBob"
    )

    // Left and Right Tear Drops falling in looping phases
    val tearProgressLeft by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "TearLeft"
    )

    val tearProgressRight by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, delayMillis = 350, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "TearRight"
    )

    Box(modifier = modifier.size(sizeDp.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val cx = width * 0.5f
            val cy = height * 0.5f + headOffsetYSin
            val headRadius = width * 0.42f

            // Face Glow Gradient
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFF80AB).copy(alpha = 0.35f), Color.Transparent),
                    center = Offset(cx, cy),
                    radius = headRadius * 1.4f
                ),
                radius = headRadius * 1.4f,
                center = Offset(cx, cy)
            )

            // Face Main Head (Soft Warm Gradient)
            drawCircle(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFD271), Color(0xFFFF9E43)),
                    startY = cy - headRadius,
                    endY = cy + headRadius
                ),
                radius = headRadius,
                center = Offset(cx, cy)
            )

            // Outer Face Border
            drawCircle(
                color = Color(0xFFE67E22),
                radius = headRadius,
                center = Offset(cx, cy),
                style = Stroke(width = 3.dp.toPx())
            )

            // Rosy Cheeks
            drawCircle(
                color = Color(0xFFFF5252).copy(alpha = 0.4f),
                radius = headRadius * 0.22f,
                center = Offset(cx - headRadius * 0.55f, cy + headRadius * 0.2f)
            )
            drawCircle(
                color = Color(0xFFFF5252).copy(alpha = 0.4f),
                radius = headRadius * 0.22f,
                center = Offset(cx + headRadius * 0.55f, cy + headRadius * 0.2f)
            )

            // Sad Eyebrows
            val browPathLeft = Path().apply {
                moveTo(cx - headRadius * 0.58f, cy - headRadius * 0.45f)
                quadraticTo(
                    cx - headRadius * 0.35f, cy - headRadius * 0.55f,
                    cx - headRadius * 0.15f, cy - headRadius * 0.38f
                )
            }
            drawPath(
                path = browPathLeft,
                color = Color(0xFF6D3200),
                style = Stroke(width = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )

            val browPathRight = Path().apply {
                moveTo(cx + headRadius * 0.15f, cy - headRadius * 0.38f)
                quadraticTo(
                    cx + headRadius * 0.35f, cy - headRadius * 0.55f,
                    cx + headRadius * 0.58f, cy - headRadius * 0.45f
                )
            }
            drawPath(
                path = browPathRight,
                color = Color(0xFF6D3200),
                style = Stroke(width = 3.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )

            // Closed Crying Eyes (Arcs: ^  ^)
            val eyePathLeft = Path().apply {
                moveTo(cx - headRadius * 0.52f, cy - headRadius * 0.12f)
                quadraticTo(
                    cx - headRadius * 0.35f, cy - headRadius * 0.3f,
                    cx - headRadius * 0.18f, cy - headRadius * 0.12f
                )
            }
            drawPath(
                path = eyePathLeft,
                color = Color(0xFF5D2400),
                style = Stroke(width = 3.5.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )

            val eyePathRight = Path().apply {
                moveTo(cx + headRadius * 0.18f, cy - headRadius * 0.12f)
                quadraticTo(
                    cx + headRadius * 0.35f, cy - headRadius * 0.3f,
                    cx + headRadius * 0.52f, cy - headRadius * 0.12f
                )
            }
            drawPath(
                path = eyePathRight,
                color = Color(0xFF5D2400),
                style = Stroke(width = 3.5.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )

            // Trembling Sad Mouth (inverted curved arc)
            val mouthPath = Path().apply {
                moveTo(cx - headRadius * 0.28f, cy + headRadius * 0.52f)
                quadraticTo(
                    cx, cy + headRadius * 0.3f,
                    cx + headRadius * 0.28f, cy + headRadius * 0.52f
                )
            }
            drawPath(
                path = mouthPath,
                color = Color(0xFF5D2400),
                style = Stroke(width = 3.5.dp.toPx(), cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )

            // Animated Falling Tear Left
            val tearStartXLeft = cx - headRadius * 0.38f
            val tearStartYLeft = cy - headRadius * 0.05f
            val tearCurrentYLeft = tearStartYLeft + (headRadius * 1.3f) * tearProgressLeft
            val tearAlphaLeft = (1f - tearProgressLeft).coerceIn(0f, 1f)

            drawTearDrop(
                cx = tearStartXLeft,
                cy = tearCurrentYLeft,
                size = 11.dp.toPx(),
                alpha = tearAlphaLeft
            )

            // Animated Falling Tear Right
            val tearStartXRight = cx + headRadius * 0.38f
            val tearStartYRight = cy - headRadius * 0.05f
            val tearCurrentYRight = tearStartYRight + (headRadius * 1.3f) * tearProgressRight
            val tearAlphaRight = (1f - tearProgressRight).coerceIn(0f, 1f)

            drawTearDrop(
                cx = tearStartXRight,
                cy = tearCurrentYRight,
                size = 11.dp.toPx(),
                alpha = tearAlphaRight
            )
        }
    }
}

private fun DrawScope.drawTearDrop(cx: Float, cy: Float, size: Float, alpha: Float) {
    if (alpha <= 0.01f) return
    val path = Path().apply {
        moveTo(cx, cy - size * 0.7f)
        cubicTo(
            cx + size * 0.55f, cy,
            cx + size * 0.55f, cy + size * 0.6f,
            cx, cy + size * 0.6f
        )
        cubicTo(
            cx - size * 0.55f, cy + size * 0.6f,
            cx - size * 0.55f, cy,
            cx, cy - size * 0.7f
        )
        close()
    }
    drawPath(path, Color(0xFF29B6F6).copy(alpha = alpha))
    drawPath(path, Color(0xFFE1F5FE).copy(alpha = alpha * 0.6f), style = Stroke(width = 1.dp.toPx()))
}
