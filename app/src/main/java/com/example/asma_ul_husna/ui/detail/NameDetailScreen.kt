package com.example.asma_ul_husna.ui.detail

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
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
import com.example.asma_ul_husna.ui.theme.DeepNavy
import com.example.asma_ul_husna.ui.theme.EnglishFontFamily
import com.example.asma_ul_husna.ui.theme.FavoriteRed
import com.example.asma_ul_husna.ui.theme.GlassBorder
import com.example.asma_ul_husna.ui.theme.GlassSurface
import com.example.asma_ul_husna.ui.theme.IslamicGold
import com.example.asma_ul_husna.ui.theme.TextPrimary
import com.example.asma_ul_husna.ui.theme.TextSecondary

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.name?.transliteration ?: stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back),
                            tint = IslamicGold
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
                                tint = if (uiState.isFavorite) FavoriteRed else TextSecondary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepNavy,
                    titleContentColor = TextPrimary
                )
            )
        },
        containerColor = DeepNavy,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DeepNavy)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = IslamicGold,
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

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Quick navigation: Previous and Next
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = onPrevClick,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(0.8.dp, IslamicGold.copy(alpha = 0.4f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = GlassSurface,
                                contentColor = IslamicGold
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "#${if (name.id <= 1) 99 else name.id - 1}", style = MaterialTheme.typography.labelMedium)
                        }

                        // Number Pill
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = IslamicGold.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "${name.id} / 99",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicGold
                                ),
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }

                        OutlinedButton(
                            onClick = onNextClick,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(0.8.dp, IslamicGold.copy(alpha = 0.4f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = GlassSurface,
                                contentColor = IslamicGold
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(text = "#${if (name.id >= 99) 1 else name.id + 1}", style = MaterialTheme.typography.labelMedium)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Hero Card (Glass translucent card)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = GlassSurface),
                        border = BorderStroke(
                            1.dp,
                            Brush.verticalGradient(
                                listOf(IslamicGold.copy(alpha = 0.45f), IslamicGold.copy(alpha = 0.12f))
                            )
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color(0x0AFFFFFF), Color(0x00FFFFFF))
                                    )
                                )
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Large Arabic Name (RTL, Serif font, Gold)
                            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                                Text(
                                    text = name.arabic,
                                    style = MaterialTheme.typography.displayMedium.copy(
                                        fontFamily = ArabicFontFamily,
                                        fontSize = 38.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BrightGold,
                                        textAlign = TextAlign.Center,
                                        textDirection = TextDirection.Rtl,
                                        lineHeight = 52.sp
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Transliteration
                            Text(
                                text = name.transliteration,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontFamily = EnglishFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    textAlign = TextAlign.Center
                                )
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Quick meaning summary
                            Text(
                                text = name.englishMeaning,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontFamily = EnglishFontFamily,
                                    color = TextSecondary,
                                    textAlign = TextAlign.Center
                                )
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            // Action buttons row: Audio & Favorite
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val isCurrentAudioLoading = uiState.isAudioUrlLoading || (uiState.playbackState is AudioPlaybackState.Loading && uiState.playbackState.id == name.id)
                                val isCurrentAudioPlaying = uiState.playbackState is AudioPlaybackState.Playing && uiState.playbackState.id == name.id

                                // Audio Button
                                Button(
                                    onClick = {
                                        if (isCurrentAudioPlaying) {
                                             onPauseAudio()
                                        } else {
                                            onPlayAudio()
                                        }
                                    },
                                    enabled = !isCurrentAudioLoading,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = IslamicGold,
                                        contentColor = DeepNavy,
                                        disabledContainerColor = IslamicGold.copy(alpha = 0.6f),
                                        disabledContentColor = DeepNavy.copy(alpha = 0.7f)
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    if (isCurrentAudioLoading) {
                                        CircularProgressIndicator(
                                            color = DeepNavy,
                                            strokeWidth = 2.dp,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = stringResource(R.string.action_loading_audio),
                                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                            maxLines = 1
                                        )
                                    } else {
                                        Icon(
                                            imageVector = if (isCurrentAudioPlaying)
                                                 Icons.Filled.Pause
                                            else
                                                 Icons.Filled.PlayArrow,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (isCurrentAudioPlaying)
                                                stringResource(R.string.action_pause_audio)
                                            else
                                                stringResource(R.string.action_play_audio),
                                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                            maxLines = 1
                                        )
                                    }
                                }

                                // Favorite Toggle Button
                                OutlinedButton(
                                    onClick = onFavoriteToggle,
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(
                                        1.dp,
                                        if (uiState.isFavorite) FavoriteRed else IslamicGold.copy(alpha = 0.4f)
                                    ),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = GlassSurface,
                                        contentColor = if (uiState.isFavorite) FavoriteRed else TextPrimary
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = if (uiState.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                        contentDescription = null,
                                        tint = if (uiState.isFavorite) FavoriteRed else TextPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = stringResource(
                                            if (uiState.isFavorite) R.string.action_remove_favorite else R.string.action_add_favorite
                                        ),
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                        maxLines = 1
                                    )
                                }
                            }

                            // Optional inline audio error notice
                            val audioErrorMessage = uiState.audioError ?: (uiState.playbackState as? AudioPlaybackState.Error)?.takeIf { it.id == name.id }?.message
                            if (audioErrorMessage != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = FavoriteRed.copy(alpha = 0.12f),
                                    border = BorderStroke(0.5.dp, FavoriteRed.copy(alpha = 0.35f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = audioErrorMessage,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextPrimary,
                                            textAlign = TextAlign.Center
                                        ),
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // English Section Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GlassSurface),
                        border = BorderStroke(1.dp, GlassBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.meaning_english_title),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicGold,
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = name.englishMeaning,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = stringResource(R.string.explanation_english_title),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicGold,
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = name.explanation,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = TextSecondary,
                                    lineHeight = 22.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Urdu Section Card (RTL Aware)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GlassSurface),
                        border = BorderStroke(1.dp, GlassBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.meaning_urdu_title),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = IslamicGold,
                                        textDirection = TextDirection.Rtl
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = name.nameUrdu,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 19.sp,
                                        color = TextPrimary,
                                        textDirection = TextDirection.Rtl
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = stringResource(R.string.explanation_urdu_title),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = IslamicGold,
                                        textDirection = TextDirection.Rtl
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = name.meaningUrdu,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = 16.5.sp,
                                        color = TextSecondary,
                                        lineHeight = 27.sp,
                                        textDirection = TextDirection.Rtl
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D1B2A)
@Composable
fun NameDetailPreview() {
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

