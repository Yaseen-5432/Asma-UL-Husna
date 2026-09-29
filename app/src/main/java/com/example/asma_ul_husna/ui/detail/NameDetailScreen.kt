package com.example.asma_ul_husna.ui.detail

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asma_ul_husna.R
import com.example.asma_ul_husna.audio.AudioPlaybackState
import com.example.asma_ul_husna.data.model.AsmaName
import com.example.asma_ul_husna.ui.components.ErrorStateView
import com.example.asma_ul_husna.ui.theme.ArabicFontFamily
import com.example.asma_ul_husna.ui.theme.AsmaulHusnaTheme
import com.example.asma_ul_husna.ui.theme.BrightGold
import com.example.asma_ul_husna.ui.theme.CardWhite
import com.example.asma_ul_husna.ui.theme.DeepIndigo
import com.example.asma_ul_husna.ui.theme.DeepIndigoDark
import com.example.asma_ul_husna.ui.theme.EnglishFontFamily
import com.example.asma_ul_husna.ui.theme.FavoriteRed
import com.example.asma_ul_husna.ui.theme.IslamicGold
import com.example.asma_ul_husna.ui.theme.PaleGold
import com.example.asma_ul_husna.ui.theme.PrimaryPurple
import com.example.asma_ul_husna.ui.theme.PrimaryPurpleLight
import com.example.asma_ul_husna.ui.theme.TextOnDark
import com.example.asma_ul_husna.ui.theme.TextOnDarkSecondary
import com.example.asma_ul_husna.ui.theme.TextPrimary
import com.example.asma_ul_husna.ui.theme.TextSecondary
import com.example.asma_ul_husna.ui.theme.getCardAccentTheme

@Composable
fun NameDetailScreen(
    viewModel: NameDetailViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    NameDetailContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onFavoriteToggle = viewModel::toggleFavorite,
        onPlayAudio = viewModel::playAudio,
        onPauseAudio = viewModel::pauseAudio,
        onNextClick = viewModel::navigateToNext,
        onPrevClick = viewModel::navigateToPrevious,
        onRetry = { uiState.name?.let { viewModel.loadName(it.id) } },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameDetailContent(
    uiState: NameDetailUiState,
    onBackClick: () -> Unit,
    onFavoriteToggle: () -> Unit,
    onPlayAudio: () -> Unit,
    onPauseAudio: () -> Unit,
    onNextClick: () -> Unit,
    onPrevClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val heartScale by animateFloatAsState(
        targetValue = if (uiState.isFavorite) 1.2f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "detailHeartScale"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.name?.transliteration ?: stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextOnDark
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back),
                            tint = BrightGold
                        )
                    }
                },
                actions = {
                    uiState.name?.let {
                        IconButton(onClick = onFavoriteToggle) {
                            Icon(
                                imageVector = if (uiState.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = stringResource(
                                    if (uiState.isFavorite) R.string.action_remove_favorite else R.string.action_add_favorite
                                ),
                                tint = if (uiState.isFavorite) FavoriteRed else TextOnDarkSecondary,
                                modifier = Modifier.scale(heartScale)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepIndigo,
                    titleContentColor = TextOnDark
                )
            )
        },
        containerColor = DeepIndigo,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DeepIndigo)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = BrightGold,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (uiState.error != null || uiState.name == null) {
                ErrorStateView(
                    message = uiState.error ?: stringResource(R.string.error_loading_names),
                    onRetry = onRetry,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                val name = uiState.name
                val accent = getCardAccentTheme(name.id)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Quick Navigation Bar: Previous (#01) ... #02 / 99 ... Next (#03)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = onPrevClick,
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.White.copy(alpha = 0.08f),
                                contentColor = BrightGold
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "#${if (name.id <= 1) 99 else name.id - 1}",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        // Number Badge Pill
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = PrimaryPurple.copy(alpha = 0.35f),
                            border = BorderStroke(1.dp, BrightGold.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "${name.id} / 99",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = BrightGold
                                ),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }

                        OutlinedButton(
                            onClick = onNextClick,
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.White.copy(alpha = 0.08f),
                                contentColor = BrightGold
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "#${if (name.id >= 99) 1 else name.id + 1}",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 1. Hero Presentation Card (Large Arabic Calligraphy + Transliteration + Audio Player)
                    Card(
                        modifier = Modifier
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
                                .padding(24.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Large Arabic Name
                                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                                    Text(
                                        text = name.arabic,
                                        style = MaterialTheme.typography.displayMedium.copy(
                                            fontFamily = ArabicFontFamily,
                                            fontSize = 42.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BrightGold,
                                            textAlign = TextAlign.Center,
                                            textDirection = TextDirection.Rtl,
                                            lineHeight = 56.sp
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Transliteration
                                Text(
                                    text = name.transliteration,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontFamily = EnglishFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        color = TextOnDark,
                                        textAlign = TextAlign.Center
                                    )
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // Quick English Meaning
                                Text(
                                    text = name.englishMeaning,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontFamily = EnglishFontFamily,
                                        color = TextOnDarkSecondary,
                                        textAlign = TextAlign.Center
                                    )
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                // Audio Playback & Action Controls Row
                                val isCurrentAudioLoading = uiState.isAudioUrlLoading || (uiState.playbackState is AudioPlaybackState.Loading && uiState.playbackState.id == name.id)
                                val isCurrentAudioPlaying = uiState.playbackState is AudioPlaybackState.Playing && uiState.playbackState.id == name.id

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        onClick = {
                                            if (isCurrentAudioPlaying) {
                                                onPauseAudio()
                                            } else {
                                                onPlayAudio()
                                            }
                                        },
                                        enabled = !isCurrentAudioLoading,
                                        shape = RoundedCornerShape(16.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = IslamicGold,
                                            contentColor = DeepIndigoDark
                                        ),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        if (isCurrentAudioLoading) {
                                            CircularProgressIndicator(
                                                color = DeepIndigoDark,
                                                strokeWidth = 2.dp,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = stringResource(R.string.action_loading_audio),
                                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                            )
                                        } else {
                                            Icon(
                                                imageVector = if (isCurrentAudioPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                                                contentDescription = null,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = if (isCurrentAudioPlaying)
                                                    stringResource(R.string.action_pause_audio)
                                                else
                                                    stringResource(R.string.action_play_audio),
                                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                                            )
                                        }
                                    }
                                }

                                // Audio Error Message if any
                                val audioErrorMessage = uiState.audioError ?: (uiState.playbackState as? AudioPlaybackState.Error)?.takeIf { it.id == name.id }?.message
                                if (audioErrorMessage != null) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = FavoriteRed.copy(alpha = 0.2f),
                                        border = BorderStroke(0.5.dp, FavoriteRed.copy(alpha = 0.5f)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = audioErrorMessage,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = TextOnDark,
                                                textAlign = TextAlign.Center
                                            ),
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 2. English Meaning & Explanation Card (Modern White Card)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 6.dp,
                                shape = RoundedCornerShape(22.dp),
                                ambientColor = Color(0x14000000),
                                spotColor = Color(0x1F000000)
                            ),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = accent.background
                            ) {
                                Text(
                                    text = stringResource(R.string.meaning_english_title),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = accent.primary,
                                        letterSpacing = 0.5.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = name.englishMeaning,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = accent.background
                            ) {
                                Text(
                                    text = stringResource(R.string.explanation_english_title),
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = accent.primary,
                                        letterSpacing = 0.5.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = name.explanation,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = TextSecondary,
                                    lineHeight = 24.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 3. Urdu Meaning & Explanation Card (Modern White Card, RTL)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 6.dp,
                                shape = RoundedCornerShape(22.dp),
                                ambientColor = Color(0x14000000),
                                spotColor = Color(0x1F000000)
                            ),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = accent.background
                                ) {
                                    Text(
                                        text = stringResource(R.string.meaning_urdu_title),
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = accent.primary,
                                            textDirection = TextDirection.Rtl
                                        ),
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = name.nameUrdu,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        textDirection = TextDirection.Rtl,
                                        fontSize = 22.sp
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = accent.background
                                ) {
                                    Text(
                                        text = stringResource(R.string.explanation_urdu_title),
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = accent.primary,
                                            textDirection = TextDirection.Rtl
                                        ),
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = name.meaningUrdu,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = TextSecondary,
                                        lineHeight = 28.sp,
                                        fontSize = 17.sp,
                                        textDirection = TextDirection.Rtl
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(36.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E174F)
@Composable
fun NameDetailModernPreview() {
    AsmaulHusnaTheme {
        NameDetailContent(
            uiState = NameDetailUiState(
                name = AsmaName(
                    id = 1,
                    arabic = "الرَّحْمَنُ",
                    transliteration = "Ar-Rahman",
                    nameUrdu = "نہایت مہربان",
                    englishMeaning = "The Most Gracious",
                    meaningUrdu = "وہ ذات جس کی رحمت تمام مخلوقات کو اس دنیا میں عام ہے اور سب کو بن مانگے نوازتا ہے۔",
                    explanation = "He whose endless mercy encompasses all creation in this worldly life, granting sustenance and blessings to all without exception.",
                    audioFilename = "audio_1.mp3"
                ),
                isFavorite = true,
                isLoading = false
            ),
            onBackClick = {},
            onFavoriteToggle = {},
            onPlayAudio = {},
            onPauseAudio = {},
            onNextClick = {},
            onPrevClick = {},
            onRetry = {}
        )
    }
}
