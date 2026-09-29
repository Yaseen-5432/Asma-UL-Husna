package com.example.asma_ul_husna.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asma_ul_husna.R
import com.example.asma_ul_husna.data.model.AsmaName
import com.example.asma_ul_husna.ui.theme.ArabicFontFamily
import com.example.asma_ul_husna.ui.theme.BrightGold
import com.example.asma_ul_husna.ui.theme.DeepIndigoDark
import com.example.asma_ul_husna.ui.theme.EnglishFontFamily
import com.example.asma_ul_husna.ui.theme.IslamicGold
import com.example.asma_ul_husna.ui.theme.PaleGold
import com.example.asma_ul_husna.ui.theme.PrimaryPurple
import com.example.asma_ul_husna.ui.theme.PrimaryPurpleLight
import com.example.asma_ul_husna.ui.theme.appColors

/**
 * Dedicated Floating 3D Centerpiece Presentation Layer for Full Recitation.
 *
 * Renders as an elevated, focused centerpiece overlay above the subdued grid during
 * synchronized recitation playback, featuring deterministic 3D entrances, continuous
 * levitating floating motion, golden halo glow, and animated shimmer borders.
 */
@Composable
fun RecitationPresentationOverlay(
    activeName: AsmaName?,
    isPlaying: Boolean,
    isPaused: Boolean,
    onTogglePlayPause: () -> Unit,
    onDismiss: () -> Unit,
    onNameClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.appColors
    val isVisible = activeName != null

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(350)) + scaleIn(tween(350, easing = FastOutSlowInEasing), initialScale = 0.88f),
        exit = fadeOut(tween(250)) + scaleOut(tween(250), targetScale = 0.9f),
        modifier = modifier.fillMaxSize()
    ) {
        if (activeName == null) return@AnimatedVisibility

        val animStyle = remember(activeName.id) { getAnimationStyleForName(activeName.id) }
        val entranceProgress = remember(activeName.id) { Animatable(0.0f) }

        LaunchedEffect(activeName.id) {
            entranceProgress.snapTo(0.0f)
            entranceProgress.animateTo(
                targetValue = 1.0f,
                animationSpec = tween(
                    durationMillis = 650,
                    easing = FastOutSlowInEasing
                )
            )
        }

        // Infinite Floating Levitation & Glow Pulsing (Active only while Playing)
        val (floatHoverY, glowPulse, shimmerOffset) = if (isPlaying) {
            val infiniteTransition = rememberInfiniteTransition(label = "overlayFloatingTransition")

            val hoverY by infiniteTransition.animateFloat(
                initialValue = -6.0f,
                targetValue = 5.0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 2600, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "overlayHoverY"
            )

            val pulse by infiniteTransition.animateFloat(
                initialValue = 0.55f,
                targetValue = 1.0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "overlayPulse"
            )

            val shimmer by infiniteTransition.animateFloat(
                initialValue = 0.0f,
                targetValue = 1.0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 3500, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "overlayShimmer"
            )

            Triple(hoverY, pulse, shimmer)
        } else {
            Triple(0.0f, 0.75f, 0.25f)
        }

        val entrance = calculateEntranceTransform(
            style = animStyle,
            progress = entranceProgress.value,
            targetScale = 1.0f
        )

        val compositeTranslationY = entrance.translationY + (floatHoverY * entranceProgress.value).dp
        val compositeRotationZ = entrance.rotationZ + (if (isPlaying) floatHoverY * 0.15f * entranceProgress.value else 0.0f)
        val haloAlpha = glowPulse * entranceProgress.value

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.68f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { /* Consume backdrop clicks */ },
            contentAlignment = Alignment.Center
        ) {
            // 1. Ambient Golden Halo Backdrop
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .height(380.dp)
                    .graphicsLayer {
                        scaleX = entrance.scale * 1.15f
                        scaleY = entrance.scale * 1.15f
                        translationX = entrance.translationX.toPx()
                        translationY = compositeTranslationY.toPx()
                        alpha = haloAlpha
                    }
                    .background(
                        brush = createGoldGlowBrush(haloAlpha),
                        shape = RoundedCornerShape(32.dp)
                    )
            )

            // 2. The 3D Floating Centerpiece Card
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(28.dp),
                        ambientColor = BrightGold.copy(alpha = 0.6f),
                        spotColor = IslamicGold
                    )
                    .graphicsLayer {
                        scaleX = entrance.scale
                        scaleY = entrance.scale
                        translationX = entrance.translationX.toPx()
                        translationY = compositeTranslationY.toPx()
                        rotationX = entrance.rotationX
                        rotationY = entrance.rotationY
                        rotationZ = compositeRotationZ
                        alpha = entrance.alpha
                        cameraDistance = 16.0f * density
                    }
                    .clip(RoundedCornerShape(28.dp))
                    .clickable { onNameClick(activeName.id) },
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = if (colors.isDark) colors.elevatedCardBackground else DeepIndigoDark),
                border = BorderStroke(
                    width = 2.dp,
                    brush = createShimmerGoldBorder(shimmerOffset)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = if (colors.isDark) {
                                    listOf(
                                        colors.elevatedCardGradientEnd,
                                        colors.elevatedCardBackground,
                                        colors.elevatedCardBackground
                                    )
                                } else {
                                    listOf(
                                        PrimaryPurple.copy(alpha = 0.9f),
                                        DeepIndigoDark,
                                        DeepIndigoDark
                                    )
                                }
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Top Bar: #ID Pill, Equalizer Icon, and Dismiss Button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = colors.primaryGold.copy(alpha = 0.25f),
                                border = BorderStroke(1.dp, colors.lightGold.copy(alpha = 0.6f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "#${activeName.id} / 99",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = colors.lightGold
                                        )
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isPlaying) {
                                    Icon(
                                        imageVector = Icons.Filled.GraphicEq,
                                        contentDescription = null,
                                        tint = colors.lightGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                }

                                IconButton(
                                    onClick = onDismiss,
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Dismiss overlay",
                                        tint = colors.textSecondary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Large Arabic Calligraphy Header
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                            Text(
                                text = activeName.arabic,
                                style = MaterialTheme.typography.displayLarge.copy(
                                    fontFamily = ArabicFontFamily,
                                    fontSize = 46.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (colors.isDark) colors.textPrimary else BrightGold,
                                    textAlign = TextAlign.Center,
                                    textDirection = TextDirection.Rtl,
                                    lineHeight = 60.sp
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Transliteration
                        Text(
                            text = activeName.transliteration,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontFamily = EnglishFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = colors.textOnBackground,
                                textAlign = TextAlign.Center,
                                letterSpacing = 0.4.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Urdu Name
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                            Text(
                                text = activeName.nameUrdu,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = colors.lightGold,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                    textDirection = TextDirection.Rtl,
                                    fontSize = 18.sp
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // English Meaning
                        Text(
                            text = activeName.englishMeaning,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = EnglishFontFamily,
                                color = colors.textSecondary,
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            ),
                            maxLines = 2
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Bottom Control Capsule: Play/Pause Full Recitation
                        Surface(
                            onClick = onTogglePlayPause,
                            shape = RoundedCornerShape(20.dp),
                            color = IslamicGold,
                            contentColor = DeepIndigoDark
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 22.dp, vertical = 9.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isPlaying) stringResource(R.string.action_pause_recitation) else stringResource(R.string.action_resume_recitation),
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
