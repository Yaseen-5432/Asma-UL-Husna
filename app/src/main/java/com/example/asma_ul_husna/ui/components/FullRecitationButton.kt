package com.example.asma_ul_husna.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asma_ul_husna.R
import com.example.asma_ul_husna.ui.theme.BrightGold
import com.example.asma_ul_husna.ui.theme.DeepIndigoDark
import com.example.asma_ul_husna.ui.theme.IslamicGold
import com.example.asma_ul_husna.ui.theme.PrimaryPurple
import com.example.asma_ul_husna.ui.theme.PrimaryPurpleLight
import com.example.asma_ul_husna.ui.theme.appColors

/**
 * Compact, modern, and attractive action button for Full Recitation.
 * Positioned directly below the "Name of the Day" card.
 */
@Composable
fun FullRecitationButton(
    isPlaying: Boolean,
    isPaused: Boolean,
    isLoading: Boolean,
    onToggleRecitation: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.appColors
    val infiniteTransition = rememberInfiniteTransition(label = "recitationButtonPulse")

    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseGlow"
    )

    val buttonScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.025f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "buttonScale"
    )

    val currentScale = if (isPlaying) buttonScale else 1.0f

    val buttonBorder = if (colors.isDark) {
        BorderStroke(1.dp, if (isPlaying) colors.primaryGold else colors.primaryGold.copy(alpha = 0.8f))
    } else {
        null
    }

    val backgroundBrush = if (colors.isDark) {
        if (isPlaying) {
            Brush.horizontalGradient(
                colors = listOf(
                    colors.primaryIndigo,
                    colors.primaryIndigo.copy(alpha = 0.85f),
                    colors.primaryIndigo
                )
            )
        } else {
            Brush.horizontalGradient(
                colors = listOf(
                    colors.elevatedCardBackground,
                    colors.elevatedCardBackground
                )
            )
        }
    } else {
        if (isPlaying) {
            Brush.horizontalGradient(
                colors = listOf(
                    PrimaryPurple,
                    PrimaryPurpleLight,
                    PrimaryPurple
                )
            )
        } else if (isPaused) {
            Brush.horizontalGradient(
                colors = listOf(
                    PrimaryPurple.copy(alpha = 0.9f),
                    DeepIndigoDark
                )
            )
        } else {
            Brush.horizontalGradient(
                colors = listOf(
                    PrimaryPurple,
                    PrimaryPurple.copy(alpha = 0.85f)
                )
            )
        }
    }

    Surface(
        onClick = onToggleRecitation,
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent,
        border = buttonBorder,
        modifier = modifier
            .fillMaxWidth()
            .scale(currentScale)
            .shadow(
                elevation = if (isPlaying) 10.dp else 4.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = if (isPlaying) colors.lightGold.copy(alpha = 0.4f) else Color(0x1A000000),
                spotColor = if (isPlaying) colors.primaryGold else Color(0x33000000)
            )
            .clip(RoundedCornerShape(20.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundBrush)
                .background(
                    if (isPlaying) {
                        Brush.radialGradient(
                            colors = listOf(
                                colors.lightGold.copy(alpha = 0.25f * pulseGlow),
                                Color.Transparent
                            )
                        )
                    } else {
                        Brush.linearGradient(listOf(Color.Transparent, Color.Transparent))
                    }
                )
                .padding(horizontal = 20.dp, vertical = 13.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = colors.lightGold,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(R.string.action_loading_audio),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = colors.lightGold,
                            letterSpacing = 0.3.sp
                        )
                    )
                } else {
                    // Modern Icon Container
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(
                                color = if (isPlaying) colors.lightGold else (if (colors.isDark) colors.primaryGold.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.15f)),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedContent(
                            targetState = isPlaying to isPaused,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "buttonIcon"
                        ) { (playing, paused) ->
                            Icon(
                                imageVector = when {
                                    playing -> Icons.Filled.Pause
                                    paused -> Icons.Filled.PlayArrow
                                    else -> Icons.Filled.PlayArrow
                                },
                                contentDescription = null,
                                tint = if (isPlaying) (if (colors.isDark) Color(0xFF17142B) else DeepIndigoDark) else colors.lightGold,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = when {
                            isPlaying -> stringResource(R.string.full_recitation_title)
                            isPaused -> stringResource(R.string.action_resume_recitation)
                            else -> stringResource(R.string.action_play_all)
                        },
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isPlaying) colors.lightGold else (if (colors.isDark) colors.lightGold else Color.White),
                            letterSpacing = 0.4.sp
                        )
                    )

                    if (isPlaying) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            imageVector = Icons.Filled.GraphicEq,
                            contentDescription = null,
                            tint = colors.lightGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
