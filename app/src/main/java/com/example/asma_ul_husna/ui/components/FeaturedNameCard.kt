package com.example.asma_ul_husna.ui.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.asma_ul_husna.ui.theme.AccentGold
import com.example.asma_ul_husna.ui.theme.AccentGoldBg
import com.example.asma_ul_husna.ui.theme.ArabicFontFamily
import com.example.asma_ul_husna.ui.theme.CardWhite
import com.example.asma_ul_husna.ui.theme.FavoriteRed
import com.example.asma_ul_husna.ui.theme.IslamicGold
import com.example.asma_ul_husna.ui.theme.PrimaryPurple
import com.example.asma_ul_husna.ui.theme.TextPrimary
import com.example.asma_ul_husna.ui.theme.TextSecondary
import com.example.asma_ul_husna.ui.theme.appColors

/**
 * Modern Featured "Name of the Day" presentation card.
 * Highlights one of the 99 Names with large Arabic calligraphy and instant action triggers.
 */
@Composable
fun FeaturedNameCard(
    name: AsmaName,
    isFavorite: Boolean,
    onNameClick: (Int) -> Unit,
    onFavoriteToggle: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.appColors

    val cardGradient = if (colors.isDark) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF2E2718),
                colors.elevatedCardBackground
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                AccentGoldBg.copy(alpha = 0.6f),
                CardWhite
            )
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = if (colors.isDark) colors.primaryIndigo.copy(alpha = 0.15f) else Color(0x18000000),
                spotColor = if (colors.isDark) colors.primaryGold.copy(alpha = 0.25f) else Color(0x22000000)
            )
            .clip(RoundedCornerShape(24.dp))
            .clickable { onNameClick(name.id) },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = colors.elevatedCardBackground),
        border = if (colors.isDark) BorderStroke(1.dp, colors.borderSubtle) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(cardGradient)
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Row: Featured Badge and Favorite Heart
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (colors.isDark) Color(0xFF2E2718) else AccentGoldBg,
                        border = BorderStroke(0.8.dp, colors.primaryGold.copy(alpha = 0.45f))
                    ) {
                        Text(
                            text = stringResource(R.string.featured_name_badge),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (colors.isDark) colors.lightGold else AccentGold,
                                letterSpacing = 1.sp
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Number Pill
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (colors.isDark) colors.primaryIndigo.copy(alpha = 0.2f) else PrimaryPurple.copy(alpha = 0.08f)
                        ) {
                            Text(
                                text = "#${name.id}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (colors.isDark) Color(0xFF9B7CFF) else PrimaryPurple
                                ),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        IconButton(
                            onClick = { onFavoriteToggle(name.id) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = null,
                                tint = if (isFavorite) (if (colors.isDark) colors.lightGold else FavoriteRed) else (if (colors.isDark) colors.textMuted else TextSecondary.copy(alpha = 0.6f)),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Prominent Arabic Calligraphy
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Text(
                        text = name.arabic,
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontFamily = ArabicFontFamily,
                            fontSize = 34.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (colors.isDark) colors.lightGold else IslamicGold,
                            textAlign = TextAlign.Center,
                            textDirection = TextDirection.Rtl,
                            lineHeight = 44.sp
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Transliteration
                Text(
                    text = name.transliteration,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                        letterSpacing = 0.3.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(2.dp))

                // English Meaning
                Text(
                    text = name.englishMeaning,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = colors.textSecondary,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Action Pill Button (View Details)
                Surface(
                    onClick = { onNameClick(name.id) },
                    shape = RoundedCornerShape(16.dp),
                    color = colors.primaryIndigo,
                    contentColor = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.action_view_details),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
        }
    }
}
