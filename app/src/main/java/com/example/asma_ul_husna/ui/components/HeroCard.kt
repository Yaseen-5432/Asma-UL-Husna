package com.example.asma_ul_husna.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.asma_ul_husna.ui.theme.ArabicFontFamily
import com.example.asma_ul_husna.ui.theme.BrightGold
import com.example.asma_ul_husna.ui.theme.DeepIndigoDark
import com.example.asma_ul_husna.ui.theme.IslamicGold
import com.example.asma_ul_husna.ui.theme.PaleGold
import com.example.asma_ul_husna.ui.theme.PrimaryPurple
import com.example.asma_ul_husna.ui.theme.PrimaryPurpleLight
import com.example.asma_ul_husna.ui.theme.TextOnDark
import com.example.asma_ul_husna.ui.theme.TextOnDarkSecondary

/**
 * Modern Hero Card for the Asma-ul-Husna home screen.
 * Features rich gradient color blocking, Arabic calligraphy, gold accents, and clean modern typography.
 */
@Composable
fun HeroCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(26.dp),
                ambientColor = PrimaryPurple.copy(alpha = 0.4f),
                spotColor = Color(0x66000000)
            ),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(
            width = 1.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    PrimaryPurpleLight.copy(alpha = 0.6f),
                    IslamicGold.copy(alpha = 0.35f),
                    Color.Transparent
                )
            )
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            PrimaryPurple,
                            PrimaryPurple.copy(alpha = 0.85f),
                            DeepIndigoDark
                        )
                    )
                )
                .padding(22.dp)
        ) {
            // Decorative background radial circles
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.TopEnd)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                BrightGold.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.12f),
                    border = BorderStroke(0.5.dp, BrightGold.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = BrightGold,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = stringResource(R.string.hero_badge),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PaleGold,
                                letterSpacing = 1.2.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Arabic Calligraphy Header
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Text(
                        text = stringResource(R.string.hero_arabic),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontFamily = ArabicFontFamily,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrightGold,
                            textAlign = TextAlign.Center,
                            textDirection = TextDirection.Rtl,
                            lineHeight = 42.sp
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // English Title
                Text(
                    text = stringResource(R.string.hero_title),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextOnDark,
                        letterSpacing = 0.5.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Subtitle / Tagline
                Text(
                    text = stringResource(R.string.hero_subtitle),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextOnDarkSecondary,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.8.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
