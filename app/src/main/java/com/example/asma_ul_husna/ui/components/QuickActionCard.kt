package com.example.asma_ul_husna.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asma_ul_husna.R
import com.example.asma_ul_husna.ui.theme.AccentGold
import com.example.asma_ul_husna.ui.theme.AccentGoldBg
import com.example.asma_ul_husna.ui.theme.CardWhite
import com.example.asma_ul_husna.ui.theme.FavoriteRed
import com.example.asma_ul_husna.ui.theme.FavoriteRedSoft
import com.example.asma_ul_husna.ui.theme.IslamicGold
import com.example.asma_ul_husna.ui.theme.PrimaryPurple
import com.example.asma_ul_husna.ui.theme.TextPrimary
import com.example.asma_ul_husna.ui.theme.TextSecondary

/**
 * 2-Column Modern Quick Action Cards row.
 * Offers fast access to Full Recitation playback and Saved Favorites with rich visual polish.
 */
@Composable
fun QuickActionsRow(
    favoriteCount: Int,
    isRecitationPlaying: Boolean,
    isRecitationPaused: Boolean,
    isRecitationLoading: Boolean,
    onToggleRecitation: () -> Unit,
    onFavoritesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Quick Action 1: Full Recitation
        QuickActionCard(
            title = stringResource(R.string.quick_action_recitation),
            subtitle = when {
                isRecitationLoading -> stringResource(R.string.action_loading_audio)
                isRecitationPlaying -> stringResource(R.string.action_pause_recitation)
                isRecitationPaused -> stringResource(R.string.action_resume_recitation)
                else -> stringResource(R.string.action_play_all)
            },
            icon = when {
                isRecitationPlaying -> Icons.Filled.Pause
                isRecitationPaused -> Icons.Filled.PlayArrow
                else -> Icons.Filled.Headphones
            },
            iconTint = IslamicGold,
            iconBgColor = AccentGoldBg,
            isLoading = isRecitationLoading,
            isActive = isRecitationPlaying,
            onClick = onToggleRecitation,
            modifier = Modifier.weight(1f)
        )

        // Quick Action 2: Favorites
        QuickActionCard(
            title = stringResource(R.string.quick_action_favorites),
            subtitle = "$favoriteCount ${stringResource(R.string.nav_favorites)}",
            icon = Icons.Filled.Favorite,
            iconTint = FavoriteRed,
            iconBgColor = FavoriteRedSoft,
            isLoading = false,
            isActive = false,
            onClick = onFavoritesClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun QuickActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    iconBgColor: Color,
    isLoading: Boolean,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale = if (isActive) {
        val infiniteTransition = rememberInfiniteTransition(label = "quickActionPulse")
        val pulseScale by infiniteTransition.animateFloat(
            initialValue = 1.0f,
            targetValue = 1.03f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulseScale"
        )
        pulseScale
    } else {
        1.0f
    }

    Card(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = Color(0x14000000),
                spotColor = Color(0x1F000000)
            )
            .clip(RoundedCornerShape(22.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Colorful Icon Container
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(iconBgColor, shape = RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = iconTint,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                if (isActive) {
                    Icon(
                        imageVector = Icons.Filled.GraphicEq,
                        contentDescription = null,
                        tint = PrimaryPurple,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                ),
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium,
                    color = if (isActive) PrimaryPurple else TextSecondary
                ),
                maxLines = 1
            )
        }
    }
}
