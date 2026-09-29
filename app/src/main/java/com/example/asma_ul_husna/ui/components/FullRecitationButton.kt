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

    Surface(
        onClick = onToggleRecitation,
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent,
        modifier = modifier
            .fillMaxWidth()
            .scale(currentScale)
            .shadow(
                elevation = if (isPlaying) 10.dp else 4.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = if (isPlaying) BrightGold.copy(alpha = 0.4f) else Color(0x1A000000),
                spotColor = if (isPlaying) IslamicGold else Color(0x33000000)
            )
            .clip(RoundedCornerShape(20.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
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
                )
                .background(
                    if (isPlaying) {
                        Brush.radialGradient(
                            colors = listOf(
                                BrightGold.copy(alpha = 0.25f * pulseGlow),
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
                        color = BrightGold,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(R.string.action_loading_audio),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = BrightGold,
                            letterSpacing = 0.3.sp
                        )
                    )
                } else {
                    // Modern Icon Container
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(
                                color = if (isPlaying) BrightGold else Color.White.copy(alpha = 0.15f),
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
                                tint = if (isPlaying) DeepIndigoDark else BrightGold,
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
                            color = if (isPlaying) BrightGold else Color.White,
                            letterSpacing = 0.4.sp
                        )
                    )

                    if (isPlaying) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            imageVector = Icons.Filled.GraphicEq,
                            contentDescription = null,
                            tint = BrightGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
