package com.example.asma_ul_husna.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.asma_ul_husna.ui.theme.EnglishFontFamily
import com.example.asma_ul_husna.ui.theme.FavoriteRed
import com.example.asma_ul_husna.ui.theme.GlassSurface
import com.example.asma_ul_husna.ui.theme.IslamicGold
import com.example.asma_ul_husna.ui.theme.TextPrimary
import com.example.asma_ul_husna.ui.theme.TextSecondary

@Composable
fun NameCard(
    name: AsmaName,
    isFavorite: Boolean,
    onCardClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    modifier: Modifier = Modifier,
    isHighlighted: Boolean = false
) {
    val heartScale by animateFloatAsState(
        targetValue = if (isFavorite) 1.15f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "heartScale"
    )

    val heartColor by animateColorAsState(
        targetValue = if (isFavorite) FavoriteRed else TextSecondary.copy(alpha = 0.55f),
        label = "heartColor"
    )

    val cardScale by animateFloatAsState(
        targetValue = if (isHighlighted) 1.025f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cardScale"
    )

    val borderStroke = if (isHighlighted) {
        BorderStroke(
            width = 2.dp,
            brush = Brush.verticalGradient(
                colors = listOf(
                    BrightGold,
                    IslamicGold
                )
            )
        )
    } else {
        BorderStroke(
            width = 1.dp,
            brush = Brush.verticalGradient(
                colors = listOf(
                    IslamicGold.copy(alpha = 0.35f),
                    IslamicGold.copy(alpha = 0.08f)
                )
            )
        )
    }

    val containerColor = if (isHighlighted) {
        IslamicGold.copy(alpha = 0.18f)
    } else {
        GlassSurface
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(cardScale)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        border = borderStroke,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x0AFFFFFF),
                            Color(0x00FFFFFF)
                        )
                    )
                )
                .padding(14.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Row: Number badge and Favorite button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Number Badge (Glass pill with subtle gold)
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = IslamicGold.copy(alpha = 0.12f),
                        border = BorderStroke(0.5.dp, IslamicGold.copy(alpha = 0.35f))
                    ) {
                        Text(
                            text = "#${name.id}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = IslamicGold
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
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
                                .size(18.dp)
                                .scale(heartScale)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Prominent Arabic Name (RTL, Serif font, Gold)
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Text(
                        text = name.arabic,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontFamily = ArabicFontFamily,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrightGold,
                            textAlign = TextAlign.Center,
                            textDirection = TextDirection.Rtl,
                            lineHeight = 32.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Transliteration (Off-White #f8f9fa)
                Text(
                    text = name.transliteration,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontFamily = EnglishFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                // English Meaning Preview (Secondary Text #a0aec0)
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

@Preview(showBackground = true, backgroundColor = 0xFF0D1B2A)
@Composable
fun NameCardPreview() {
    AsmaulHusnaTheme {
        NameCard(
            name = AsmaName(
                id = 1,
                arabic = "الرَّحْمَنُ",
                transliteration = "Ar-Rahman",
                nameUrdu = "نہایت مہربان",
                englishMeaning = "The Most Gracious, The All-Compassionate",
                meaningUrdu = "وہ ذات جس کی رحمت تمام مخلوقات کو عام ہے۔",
                explanation = "He whose endless mercy encompasses all creation.",
                audioFilename = "audio_1.mp3"
            ),
            isFavorite = true,
            onCardClick = {},
            onFavoriteToggle = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

