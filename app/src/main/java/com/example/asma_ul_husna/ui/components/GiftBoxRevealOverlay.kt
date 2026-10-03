package com.example.asma_ul_husna.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asma_ul_husna.data.model.AsmaName
import com.example.asma_ul_husna.ui.theme.ArabicFontFamily
import com.example.asma_ul_husna.ui.theme.BrightGold
import com.example.asma_ul_husna.ui.theme.EnglishFontFamily
import com.example.asma_ul_husna.ui.theme.IslamicGold
import com.example.asma_ul_husna.ui.theme.PrimaryPurple
import com.example.asma_ul_husna.ui.theme.appColors
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private val SprinkleColors = listOf(
    Color(0xFFFFD166), // Gold
    Color(0xFFFF2E93), // Pink
    Color(0xFF00F2FE), // Cyan
    Color(0xFF9D4EDD), // Purple
    Color(0xFF06D6A0), // Emerald
    Color(0xFFFF5757), // Coral
    Color(0xFFFFFFFF), // Sparkle White
    Color(0xFFFFAA00)  // Amber
)

private enum class SprinkleType {
    STAR,
    DIAMOND,
    CIRCLE,
    RECTANGLE
}

private data class SprinkleBurstParticle(
    val angleRad: Double,
    val speed: Float,
    val color: Color,
    val type: SprinkleType,
    val size: Float,
    val rotationSpeed: Float
)

/**
 * Premium Gift-box Opening + Colorful Sprinkle Pop navigation reveal overlay.
 * Renders an engaging 3D gift-box lid opening with radial light burst and sprinkles,
 * seamlessly transitioning to the Name Detail screen.
 */
@Composable
fun GiftBoxRevealOverlay(
    name: AsmaName?,
    onAnimationComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (name == null) return

    val colors = MaterialTheme.appColors

    // Animation progress drivers
    val boxScale = remember { Animatable(0.85f) }
    val lidOffsetY = remember { Animatable(0f) }
    val lidRotation = remember { Animatable(0f) }
    val innerGlowScale = remember { Animatable(0.2f) }
    val innerGlowAlpha = remember { Animatable(0f) }
    val particleProgress = remember { Animatable(0f) }
    val contentEmergence = remember { Animatable(0f) }
    val scrimAlpha = remember { Animatable(0f) }

    val random = remember { Random(name.id * 31L) }
    val particles = remember(name.id) {
        List(38) {
            val angle = random.nextDouble(0.0, Math.PI * 2.0)
            val speed = random.nextFloat() * 260f + 120f
            SprinkleBurstParticle(
                angleRad = angle,
                speed = speed,
                color = SprinkleColors[random.nextInt(SprinkleColors.size)],
                type = SprinkleType.values()[random.nextInt(SprinkleType.values().size)],
                size = random.nextFloat() * 10f + 8f,
                rotationSpeed = (random.nextFloat() - 0.5f) * 720f
            )
        }
    }

    LaunchedEffect(name.id) {
        // Phase 1: Card / Box Press & Pop (0 - 120ms)
        scrimAlpha.animateTo(0.72f, tween(180))
        boxScale.animateTo(1.04f, spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium))

        // Phase 2: Gift Box Lid Opens & Inner Glow Expands (120 - 320ms)
        launch {
            lidOffsetY.animateTo(-85f, tween(260, easing = FastOutSlowInEasing))
        }
        launch {
            lidRotation.animateTo(-16f, tween(260, easing = FastOutSlowInEasing))
        }
        launch {
            innerGlowAlpha.animateTo(1f, tween(180))
            innerGlowScale.animateTo(1.7f, tween(320, easing = LinearOutSlowInEasing))
        }

        // Phase 3: Sprinkles Pop & Burst (220 - 520ms)
        launch {
            particleProgress.animateTo(1f, tween(380, easing = LinearOutSlowInEasing))
        }

        // Phase 4: Name Content Emerges from Box (300 - 580ms)
        launch {
            contentEmergence.animateTo(1f, spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow))
        }

        // Settle & Complete (~620ms total)
        kotlinx.coroutines.delay(620)
        onAnimationComplete()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = scrimAlpha.value))
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) {},
        contentAlignment = Alignment.Center
    ) {
        // -------------------------------------------------------------
        // Colorful Sprinkles Burst Canvas Layer
        // -------------------------------------------------------------
        if (particleProgress.value > 0f) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width * 0.5f
                val cy = size.height * 0.5f
                val p = particleProgress.value

                particles.forEach { particle ->
                    val dist = particle.speed * p
                    val px = (cx + dist * cos(particle.angleRad)).toFloat()
                    val py = (cy + dist * sin(particle.angleRad) + 60f * p * p).toFloat()
                    val alpha = (1f - p * 0.9f).coerceIn(0f, 1f)
                    val rotation = particle.rotationSpeed * p

                    rotate(rotation, Offset(px, py)) {
                        when (particle.type) {
                            SprinkleType.STAR -> {
                                drawStar(px, py, particle.size * (1f + 0.3f * p), particle.color.copy(alpha = alpha))
                            }
                            SprinkleType.DIAMOND -> {
                                val s = particle.size * 0.55f
                                val path = Path().apply {
                                    moveTo(px, py - s)
                                    lineTo(px + s, py)
                                    lineTo(px, py + s)
                                    lineTo(px - s, py)
                                    close()
                                }
                                drawPath(path, particle.color.copy(alpha = alpha))
                            }
                            SprinkleType.CIRCLE -> {
                                drawCircle(
                                    color = particle.color.copy(alpha = alpha),
                                    radius = particle.size * 0.45f,
                                    center = Offset(px, py)
                                )
                            }
                            SprinkleType.RECTANGLE -> {
                                val w = particle.size * 1.1f
                                val h = particle.size * 0.5f
                                drawRoundRect(
                                    color = particle.color.copy(alpha = alpha),
                                    topLeft = Offset(px - w * 0.5f, py - h * 0.5f),
                                    size = Size(w, h),
                                    cornerRadius = CornerRadius(2f, 2f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // Radiant Inner Light Beam
        // -------------------------------------------------------------
        if (innerGlowAlpha.value > 0f) {
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .scale(innerGlowScale.value)
                    .graphicsLayer { alpha = innerGlowAlpha.value }
                    .background(
                        Brush.radialGradient(
                            listOf(
                                BrightGold.copy(alpha = 0.85f),
                                Color(0xFFFF2E93).copy(alpha = 0.5f),
                                Color(0xFF00F2FE).copy(alpha = 0.35f),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    )
            )
        }

        // -------------------------------------------------------------
        // 3D Gift Box Body & Emerging Content
        // -------------------------------------------------------------
        Box(
            modifier = Modifier
                .scale(boxScale.value)
                .size(width = 290.dp, height = 340.dp),
            contentAlignment = Alignment.Center
        ) {
            // Box Base Container
            Card(
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (colors.isDark) Color(0xFF1F1848) else Color(0xFF281C5C)
                ),
                border = BorderStroke(2.dp, Brush.horizontalGradient(listOf(BrightGold, Color(0xFFFF2E93), Color(0xFF00F2FE)))),
                modifier = Modifier
                    .fillMaxSize()
                    .shadow(16.dp, RoundedCornerShape(26.dp), spotColor = BrightGold)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Golden Ribbon Accent lines across box
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val ribbonColor = BrightGold.copy(alpha = 0.35f)
                        val w = size.width
                        val h = size.height
                        // Vertical ribbon
                        drawRect(
                            color = ribbonColor,
                            topLeft = Offset(w * 0.5f - 10.dp.toPx(), 0f),
                            size = Size(20.dp.toPx(), h)
                        )
                    }

                    // Emerging Content Preview (Scales up & floats out of the box)
                    if (contentEmergence.value > 0.05f) {
                        val e = contentEmergence.value
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .graphicsLayer {
                                    scaleX = 0.7f + 0.3f * e
                                    scaleY = 0.7f + 0.3f * e
                                    alpha = e.coerceIn(0f, 1f)
                                    translationY = -40f * e
                                }
                                .padding(horizontal = 12.dp)
                        ) {
                            // Badge
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Brush.horizontalGradient(listOf(BrightGold, Color(0xFFFF5722))))
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "#${name.id} OF 99",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.1.sp
                                    ),
                                    color = Color(0xFF1E174F)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Large Arabic Calligraphy
                            androidx.compose.runtime.CompositionLocalProvider(
                                androidx.compose.ui.platform.LocalLayoutDirection provides androidx.compose.ui.unit.LayoutDirection.Rtl
                            ) {
                                Text(
                                    text = name.arabic,
                                    style = MaterialTheme.typography.displayMedium.copy(
                                        fontFamily = ArabicFontFamily,
                                        fontSize = 38.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BrightGold,
                                        textAlign = TextAlign.Center,
                                        textDirection = TextDirection.Rtl,
                                        lineHeight = 46.sp
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Transliteration
                            Text(
                                text = name.transliteration,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontFamily = EnglishFontFamily,
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // English Meaning
                            Text(
                                text = name.englishMeaning,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Color.White.copy(alpha = 0.85f),
                                textAlign = TextAlign.Center,
                                maxLines = 2
                            )
                        }
                    }
                }
            }

            // -------------------------------------------------------------
            // Gift Box Lid with Bow (Slides and Rotates Upward)
            // -------------------------------------------------------------
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .graphicsLayer {
                        translationY = lidOffsetY.value
                        rotationZ = lidRotation.value
                    }
                    .fillMaxWidth(1.06f)
                    .height(68.dp)
                    .shadow(10.dp, RoundedCornerShape(16.dp), spotColor = BrightGold)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF8A2387),
                                Color(0xFFE94057),
                                Color(0xFFF27121)
                            )
                        )
                    )
                    .border(2.dp, BrightGold, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Bow Icon on Lid
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AutoAwesome,
                        contentDescription = null,
                        tint = BrightGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "ASMA-UL-HUSNA",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.2.sp
                        ),
                        color = Color.White
                    )
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = BrightGold,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawStar(cx: Float, cy: Float, radius: Float, color: Color) {
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
