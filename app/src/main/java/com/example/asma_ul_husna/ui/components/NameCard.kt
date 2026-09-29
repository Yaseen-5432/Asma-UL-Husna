package com.example.asma_ul_husna.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asma_ul_husna.R
import com.example.asma_ul_husna.data.model.AsmaName
import com.example.asma_ul_husna.ui.theme.ArabicFontFamily
import com.example.asma_ul_husna.ui.theme.AsmaulHusnaTheme
import com.example.asma_ul_husna.ui.theme.BrightGold
import com.example.asma_ul_husna.ui.theme.CardWhite
import com.example.asma_ul_husna.ui.theme.EnglishFontFamily
import com.example.asma_ul_husna.ui.theme.FavoriteRed
import com.example.asma_ul_husna.ui.theme.IslamicGold
import com.example.asma_ul_husna.ui.theme.TextPrimary
import com.example.asma_ul_husna.ui.theme.TextSecondary
import com.example.asma_ul_husna.ui.theme.getCardAccentTheme
import kotlin.math.abs

/**
 * Modern, eye-catching Name Card component for Asma-ul-Husna.
 *
 * Visual language:
 * - Large rounded white card with soft elevation (Corner radius: 22dp)
 * - Controlled subtle accent system for colorful modern details
 * - Large Arabic calligraphy with Islamic gold presence
 * - Seamless 3D floating centerpiece mode during full recitation
 */
@Composable
fun NameCard(
    name: AsmaName,
    isFavorite: Boolean,
    onCardClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier,
    isHighlighted: Boolean = false,
    isRecitationActive: Boolean = false,
    isPlaying: Boolean = false,
    isPaused: Boolean = false,
    relativePosition: Int = 0
) {
    val accent = remember(name.id) { getCardAccentTheme(name.id) }

    // -----------------------------------------------------------------------------------------
    // 1. Favorite Heart Animation (Modern quick scale bounce)
    // -----------------------------------------------------------------------------------------
    val heartScale by animateFloatAsState(
        targetValue = if (isFavorite) 1.18f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "heartScale"
    )

    val heartColor by animateColorAsState(
        targetValue = if (isFavorite) FavoriteRed else TextSecondary.copy(alpha = 0.45f),
        label = "heartColor"
    )

    // -----------------------------------------------------------------------------------------
    // 2. Deterministic 3D Entrance Animation Style
    // -----------------------------------------------------------------------------------------
    val animStyle = remember(name.id) { getAnimationStyleForName(name.id) }
    val entranceProgress = remember { Animatable(if (isHighlighted) 1.0f else 0.0f) }

    LaunchedEffect(isHighlighted, name.id) {
        if (isHighlighted) {
            entranceProgress.snapTo(0.0f)
            entranceProgress.animateTo(
                targetValue = 1.0f,
                animationSpec = tween(
                    durationMillis = 650,
                    easing = FastOutSlowInEasing
                )
            )
        } else {
            entranceProgress.snapTo(0.0f)
        }
    }

    // -----------------------------------------------------------------------------------------
    // 3. Continuous Levitation & Glow Shimmer (Active Card Only)
    // -----------------------------------------------------------------------------------------
    val (floatHoverY, glowPulse, shimmerOffset) = if (isHighlighted && isPlaying) {
        val infiniteTransition = rememberInfiniteTransition(label = "floatingCardTransition")

        val hoverY by infiniteTransition.animateFloat(
            initialValue = -4.5f,
            targetValue = 3.5f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "hoverY"
        )

        val pulse by infiniteTransition.animateFloat(
            initialValue = 0.55f,
            targetValue = 0.95f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse"
        )

        val shimmer by infiniteTransition.animateFloat(
            initialValue = 0.0f,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3500, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "shimmer"
        )

        Triple(hoverY, pulse, shimmer)
    } else if (isHighlighted && isPaused) {
        Triple(0.0f, 0.75f, 0.25f)
    } else {
        Triple(0.0f, 0.0f, 0.0f)
    }

    // -----------------------------------------------------------------------------------------
    // 4. Spatial Geometry & Hierarchy Transforms
    // -----------------------------------------------------------------------------------------
    val (renderScale, renderTranslationX, renderTranslationY, renderRotationX, renderRotationY, renderRotationZ, renderAlpha, renderElevation) =
        if (isHighlighted) {
            val entrance = calculateEntranceTransform(
                style = animStyle,
                progress = entranceProgress.value,
                targetScale = 1.07f
            )

            val currentFloatProgress = entranceProgress.value
            val compositeTranslationY = entrance.translationY + (floatHoverY * currentFloatProgress).dp
            val compositeRotationZ = entrance.rotationZ + (if (isPlaying) floatHoverY * 0.18f * currentFloatProgress else 0.0f)
            val elevation = (16.0f * currentFloatProgress).dp

            FloatingCardState(
                scale = entrance.scale,
                translationX = entrance.translationX,
                translationY = compositeTranslationY,
                rotationX = entrance.rotationX,
                rotationY = entrance.rotationY,
                rotationZ = compositeRotationZ,
                alpha = entrance.alpha,
                elevation = elevation
            )
        } else {
            // Surrounding cards subdued in full recitation mode; full opacity during standard browsing
            val targetScale = when {
                !isRecitationActive -> 1.0f
                abs(relativePosition) <= 2 -> 0.88f
                else -> 0.83f
            }

            val targetAlpha = when {
                !isRecitationActive -> 1.0f
                abs(relativePosition) <= 2 -> 0.74f
                else -> 0.55f
            }

            val targetRotationY = when {
                !isRecitationActive -> 0.0f
                relativePosition < 0 -> -3.8f
                else -> 3.8f
            }

            val inactiveScale by animateFloatAsState(
                targetValue = targetScale,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMediumLow
                ),
                label = "inactiveScale"
            )

            val inactiveAlpha by animateFloatAsState(
                targetValue = targetAlpha,
                animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
                label = "inactiveAlpha"
            )

            val inactiveRotationY by animateFloatAsState(
                targetValue = targetRotationY,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "inactiveRotationY"
            )

            FloatingCardState(
                scale = inactiveScale,
                translationX = 0.dp,
                translationY = 0.dp,
                rotationX = 0.0f,
                rotationY = inactiveRotationY,
                rotationZ = 0.0f,
                alpha = inactiveAlpha,
                elevation = 5.dp
            )
        }

    // Border styling
    val borderStroke = if (isHighlighted) {
        BorderStroke(
            width = 2.dp,
            brush = createShimmerGoldBorder(shimmerOffset)
        )
    } else {
        BorderStroke(
            width = 0.5.dp,
            color = accent.border.copy(alpha = 0.35f)
        )
    }

    // -----------------------------------------------------------------------------------------
    // 5. Composition Structure: Ambient Glow Backdrop + Modern White Card
    // -----------------------------------------------------------------------------------------
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // Golden Ambient Halo Backdrop when Highlighted
        if (isHighlighted && entranceProgress.value > 0.05f) {
            val haloAlpha = glowPulse * entranceProgress.value
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        scaleX = renderScale * 1.14f
                        scaleY = renderScale * 1.14f
                        translationX = renderTranslationX.toPx()
                        translationY = renderTranslationY.toPx()
                        alpha = haloAlpha
                    }
                    .background(
                        brush = createGoldGlowBrush(haloAlpha),
                        shape = RoundedCornerShape(24.dp)
                    )
            )
        }

        // Modern White Rounded Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = renderElevation,
                    shape = RoundedCornerShape(22.dp),
                    ambientColor = if (isHighlighted) BrightGold.copy(alpha = 0.5f) else Color(0x14000000),
                    spotColor = if (isHighlighted) IslamicGold else Color(0x1F000000)
                )
                .graphicsLayer {
                    scaleX = renderScale
                    scaleY = renderScale
                    translationX = renderTranslationX.toPx()
                    translationY = renderTranslationY.toPx()
                    rotationX = renderRotationX
                    rotationY = renderRotationY
                    rotationZ = renderRotationZ
                    alpha = renderAlpha
                    cameraDistance = 16.0f * density
                }
                .clip(RoundedCornerShape(22.dp))
                .clickable(onClick = onCardClick),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = CardWhite
            ),
            border = borderStroke,
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isHighlighted) {
                            Brush.verticalGradient(
                                colors = listOf(
                                    accent.background.copy(alpha = 0.8f),
                                    CardWhite
                                )
                            )
                        } else {
                            Brush.verticalGradient(
                                colors = listOf(
                                    accent.background.copy(alpha = 0.35f),
                                    CardWhite
                                )
                            )
                        }
                    )
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top Row: Modern Number Badge and Favorite Heart Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Formatted modern badge: #01, #02, etc.
                        val formattedId = if (name.id < 10) "#0${name.id}" else "#${name.id}"
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = accent.background,
                            border = BorderStroke(0.5.dp, accent.primary.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = formattedId,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = accent.primary
                                ),
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                            )
                        }

                        // Favorite Button
                        IconButton(
                            onClick = onFavoriteToggle,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = stringResource(
                                    if (isFavorite) R.string.action_remove_favorite else R.string.action_add_favorite
                                ),
                                tint = heartColor,
                                modifier = Modifier
                                    .size(19.dp)
                                    .scale(heartScale)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Prominent Arabic Calligraphy
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        Text(
                            text = name.arabic,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontFamily = ArabicFontFamily,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = IslamicGold,
                                textAlign = TextAlign.Center,
                                textDirection = TextDirection.Rtl,
                                lineHeight = 34.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Transliteration
                    Text(
                        text = name.transliteration,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontFamily = EnglishFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            textAlign = TextAlign.Center
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // English Meaning Preview
                    Text(
                        text = name.englishMeaning,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = EnglishFontFamily,
                            fontSize = 11.5.sp,
                            color = TextSecondary,
                            textAlign = TextAlign.Center,
                            lineHeight = 15.sp
                        ),
                        maxLines = 2,
                        minLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

private data class FloatingCardState(
    val scale: Float,
    val translationX: androidx.compose.ui.unit.Dp,
    val translationY: androidx.compose.ui.unit.Dp,
    val rotationX: Float,
    val rotationY: Float,
    val rotationZ: Float,
    val alpha: Float,
    val elevation: androidx.compose.ui.unit.Dp
)

@Preview(showBackground = true, backgroundColor = 0xFF1E174F)
@Composable
fun NameCardModernPreview() {
    AsmaulHusnaTheme {
        NameCard(
            name = AsmaName(
                id = 1,
                arabic = "الرَّحْمَنُ",
                transliteration = "Ar-Rahman",
                nameUrdu = "نہایت مہربان",
                englishMeaning = "The Most Gracious",
                meaningUrdu = "وہ ذات جس کی رحمت تمام مخلوقات کو عام ہے۔",
                explanation = "He whose endless mercy encompasses all creation.",
                audioFilename = "audio_1.mp3"
            ),
            isFavorite = true,
            isHighlighted = false,
            isRecitationActive = false,
            isPlaying = false,
            onCardClick = {},
            onFavoriteToggle = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
