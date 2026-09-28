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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.asma_ul_husna.ui.theme.IslamicGold
import com.example.asma_ul_husna.ui.theme.TextPrimary
import com.example.asma_ul_husna.ui.theme.TextSecondary

@Composable
fun HeaderSection(
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false,
    isPaused: Boolean = false,
    isLoading: Boolean = false,
    onToggleRecitation: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Arabic Bismillah Header (RTL, Serif font, Gold)
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Text(
                text = stringResource(R.string.header_arabic),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = ArabicFontFamily,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrightGold,
                    textAlign = TextAlign.Center,
                    textDirection = TextDirection.Rtl,
                    lineHeight = 36.sp
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // English Title
        Text(
            text = stringResource(R.string.header_title),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                letterSpacing = 1.2.sp
            )
        )

        Spacer(modifier = Modifier.height(3.dp))

        // Subtitle
        Text(
            text = stringResource(R.string.header_subtitle),
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Ornamental Gold Divider
        Row(
            modifier = Modifier.fillMaxWidth(0.5f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.Transparent, IslamicGold.copy(alpha = 0.5f))
                        )
                    )
            )

            Spacer(modifier = Modifier.width(6.dp))

            Box(
                modifier = Modifier
                    .size(5.dp)
                    .background(IslamicGold, shape = CircleShape)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(IslamicGold.copy(alpha = 0.5f), Color.Transparent)
                        )
                    )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Full Recitation (Play All / Pause / Resume) Capsule Button
        Surface(
            onClick = onToggleRecitation,
            shape = RoundedCornerShape(24.dp),
            color = if (isPlaying) IslamicGold.copy(alpha = 0.22f) else IslamicGold.copy(alpha = 0.12f),
            border = BorderStroke(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    listOf(
                        IslamicGold.copy(alpha = if (isPlaying) 0.85f else 0.45f),
                        BrightGold.copy(alpha = if (isPlaying) 0.95f else 0.65f),
                        IslamicGold.copy(alpha = if (isPlaying) 0.85f else 0.45f)
                    )
                )
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = BrightGold,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.action_loading_audio),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = BrightGold
                        )
                    )
                } else if (isPlaying) {
                    Icon(
                        imageVector = Icons.Filled.Pause,
                        contentDescription = stringResource(R.string.action_pause_recitation),
                        tint = BrightGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.action_pause_recitation),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = BrightGold
                        )
                    )
                } else if (isPaused) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = stringResource(R.string.action_resume_recitation),
                        tint = BrightGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.action_resume_recitation),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = BrightGold
                        )
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = stringResource(R.string.action_play_all),
                        tint = BrightGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.action_play_all),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = BrightGold
                        )
                    )
                }
            }
        }
    }
}
