package com.example.asma_ul_husna.ui.detail

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
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
import com.example.asma_ul_husna.ui.theme.appColors

/**
 * Direction for horizontal slide transitions between Names.
 * FORWARD: Next Name enters from the RIGHT (+x), old exits to LEFT (-x).
 * BACKWARD: Previous Name enters from the LEFT (-x), old exits to RIGHT (+x).
 */
enum class DetailSlideDirection {
    FORWARD,
    BACKWARD
}

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
    val context = LocalContext.current
    val colors = MaterialTheme.appColors
    val scrollState = rememberScrollState()
    var slideDirection by remember { mutableStateOf(DetailSlideDirection.FORWARD) }

    val ttsManager = remember {
        ExplanationTtsManager(context) { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    val ttsState by ttsManager.state.collectAsState()

    // When the displayed Name ID changes, immediately stop any active TTS and reset state
    LaunchedEffect(uiState.name?.id) {
        ttsManager.stop()
    }

    // Lifecycle observer: Stop TTS on ON_PAUSE / ON_STOP and release on disposal
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE || event == Lifecycle.Event.ON_STOP) {
                ttsManager.stop()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            ttsManager.release()
        }
    }

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
                            color = colors.textOnBackground
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back),
                            tint = colors.lightGold
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
                                tint = if (uiState.isFavorite) (if (colors.isDark) colors.lightGold else FavoriteRed) else colors.textOnBackgroundSecondary,
                                modifier = Modifier.scale(heartScale)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.mainBackground,
                    titleContentColor = colors.textOnBackground
                )
            )
        },
        containerColor = colors.mainBackground,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(colors.mainBackground)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = colors.lightGold,
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
                    // Quick Navigation Bar: Previous (#01) ... #02 / 99 ... Next (#03)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                slideDirection = DetailSlideDirection.BACKWARD
                                onPrevClick()
                            },
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, if (colors.isDark) colors.borderSubtle else Color.White.copy(alpha = 0.2f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (colors.isDark) colors.cardBackground else Color.White.copy(alpha = 0.08f),
                                contentColor = colors.lightGold
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
                            color = if (colors.isDark) colors.elevatedCardBackground else colors.primaryIndigo.copy(alpha = 0.35f),
                            border = BorderStroke(1.dp, colors.primaryGold.copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "${name.id} / 99",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = colors.lightGold
                                ),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                slideDirection = DetailSlideDirection.FORWARD
                                onNextClick()
                            },
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, if (colors.isDark) colors.borderSubtle else Color.White.copy(alpha = 0.2f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (colors.isDark) colors.cardBackground else Color.White.copy(alpha = 0.08f),
                                contentColor = colors.lightGold
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

                    AnimatedContent(
                        targetState = name,
                        transitionSpec = {
                            if (slideDirection == DetailSlideDirection.BACKWARD) {
                                (slideInHorizontally(
                                    initialOffsetX = { -it },
                                    animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing)
                                ) + fadeIn(animationSpec = tween(320)))
                                    .togetherWith(
                                        slideOutHorizontally(
                                            targetOffsetX = { it },
                                            animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing)
                                        ) + fadeOut(animationSpec = tween(220))
                                    )
                            } else {
                                (slideInHorizontally(
                                    initialOffsetX = { it },
                                    animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing)
                                ) + fadeIn(animationSpec = tween(320)))
                                    .togetherWith(
                                        slideOutHorizontally(
                                            targetOffsetX = { -it },
                                            animationSpec = tween(durationMillis = 320, easing = FastOutSlowInEasing)
                                        ) + fadeOut(animationSpec = tween(220))
                                    )
                            }
                        },
                        label = "detailCardSlideAnimation"
                    ) { targetName ->
                        val accent = remember(targetName.id, colors.isDark) { colors.cardAccent(targetName.id) }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // 1. Hero Presentation Card (Large Arabic Calligraphy + Transliteration + Audio Player)
                            val heroBorderBrush = if (colors.isDark) {
                                Brush.linearGradient(
                                    colors = listOf(
                                        colors.primaryIndigo.copy(alpha = 0.5f),
                                        colors.primaryGold.copy(alpha = 0.35f),
                                        Color.Transparent
                                    )
                                )
                            } else {
                                Brush.linearGradient(
                                    colors = listOf(
                                        PrimaryPurpleLight.copy(alpha = 0.6f),
                                        IslamicGold.copy(alpha = 0.35f),
                                        Color.Transparent
                                    )
                                )
                            }

                            val heroBackgroundBrush = if (colors.isDark) {
                                Brush.verticalGradient(
                                    colors = listOf(
                                        colors.elevatedCardBackground,
                                        colors.elevatedCardGradientEnd,
                                        colors.cardBackground
                                    )
                                )
                            } else {
                                Brush.verticalGradient(
                                    colors = listOf(
                                        PrimaryPurple,
                                        PrimaryPurple.copy(alpha = 0.85f),
                                        DeepIndigoDark
                                    )
                                )
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 12.dp,
                                        shape = RoundedCornerShape(26.dp),
                                        ambientColor = if (colors.isDark) colors.primaryIndigo.copy(alpha = 0.2f) else PrimaryPurple.copy(alpha = 0.4f),
                                        spotColor = Color(0x66000000)
                                    ),
                                shape = RoundedCornerShape(26.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                                border = BorderStroke(
                                    width = 1.dp,
                                    brush = heroBorderBrush
                                )
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(heroBackgroundBrush)
                                        .padding(24.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        // Large Arabic Name
                                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                                            Text(
                                                text = targetName.arabic,
                                                style = MaterialTheme.typography.displayMedium.copy(
                                                    fontFamily = ArabicFontFamily,
                                                    fontSize = 42.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = colors.lightGold,
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
                                            text = targetName.transliteration,
                                            style = MaterialTheme.typography.headlineMedium.copy(
                                                fontFamily = EnglishFontFamily,
                                                fontWeight = FontWeight.Bold,
                                                color = colors.textOnBackground,
                                                textAlign = TextAlign.Center
                                            )
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        // Quick English Meaning
                                        Text(
                                            text = targetName.englishMeaning,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontFamily = EnglishFontFamily,
                                                color = colors.textOnBackgroundSecondary,
                                                textAlign = TextAlign.Center
                                            )
                                        )

                                        Spacer(modifier = Modifier.height(20.dp))

                                        // Audio Playback & Action Controls Row
                                        val isCurrentAudioLoading = uiState.isAudioUrlLoading || (uiState.playbackState is AudioPlaybackState.Loading && uiState.playbackState.id == targetName.id)
                                        val isCurrentAudioPlaying = uiState.playbackState is AudioPlaybackState.Playing && uiState.playbackState.id == targetName.id

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Button(
                                                onClick = {
                                                    ttsManager.stop()
                                                    if (isCurrentAudioPlaying) {
                                                        onPauseAudio()
                                                    } else {
                                                        onPlayAudio()
                                                    }
                                                },
                                                enabled = !isCurrentAudioLoading,
                                                shape = RoundedCornerShape(16.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = colors.primaryGold,
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
                                        val audioErrorMessage = uiState.audioError ?: (uiState.playbackState as? AudioPlaybackState.Error)?.takeIf { it.id == targetName.id }?.message
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
                                                        color = colors.textOnBackground,
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

                            // 2. English Meaning & Explanation Card
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 6.dp,
                                        shape = RoundedCornerShape(22.dp),
                                        ambientColor = if (colors.isDark) Color(0x22000000) else Color(0x14000000),
                                        spotColor = if (colors.isDark) Color(0x33000000) else Color(0x1F000000)
                                    ),
                                shape = RoundedCornerShape(22.dp),
                                colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
                                border = if (colors.isDark) BorderStroke(1.dp, colors.borderSubtle) else null,
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
                                        text = targetName.englishMeaning,
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = colors.textPrimary
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    // Explanation Header Row with Listen / TTS button
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
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

                                        ExplanationTtsButton(
                                            isSpeaking = ttsState.isSpeaking &&
                                                ttsState.activeLanguage == ExplanationTtsLanguage.ENGLISH &&
                                                ttsState.activeNameId == targetName.id,
                                            onClick = {
                                                ttsManager.speak(
                                                    nameId = targetName.id,
                                                    text = targetName.explanation,
                                                    language = ExplanationTtsLanguage.ENGLISH,
                                                    onPlaybackStarted = { onPauseAudio() }
                                                )
                                            },
                                            accentColor = accent.primary,
                                            accentBackground = accent.background,
                                            isDark = colors.isDark,
                                            textListen = stringResource(R.string.action_tts_listen),
                                            textStop = stringResource(R.string.action_tts_stop)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = targetName.explanation,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            color = colors.textSecondary,
                                            lineHeight = 24.sp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // 3. Urdu Meaning & Explanation Card (RTL)
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 6.dp,
                                        shape = RoundedCornerShape(22.dp),
                                        ambientColor = if (colors.isDark) Color(0x22000000) else Color(0x14000000),
                                        spotColor = if (colors.isDark) Color(0x33000000) else Color(0x1F000000)
                                    ),
                                shape = RoundedCornerShape(22.dp),
                                colors = CardDefaults.cardColors(containerColor = colors.cardBackground),
                                border = if (colors.isDark) BorderStroke(1.dp, colors.borderSubtle) else null,
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
                                            text = targetName.nameUrdu,
                                            style = MaterialTheme.typography.headlineSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = colors.textPrimary,
                                                textDirection = TextDirection.Rtl,
                                                fontSize = 22.sp
                                            ),
                                            modifier = Modifier.fillMaxWidth()
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Urdu Explanation Header Row with Listen / TTS button (RTL)
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
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

                                            ExplanationTtsButton(
                                                isSpeaking = ttsState.isSpeaking &&
                                                    ttsState.activeLanguage == ExplanationTtsLanguage.URDU &&
                                                    ttsState.activeNameId == targetName.id,
                                                onClick = {
                                                    ttsManager.speak(
                                                        nameId = targetName.id,
                                                        text = targetName.meaningUrdu,
                                                        language = ExplanationTtsLanguage.URDU,
                                                        onPlaybackStarted = { onPauseAudio() }
                                                    )
                                                },
                                                accentColor = accent.primary,
                                                accentBackground = accent.background,
                                                isDark = colors.isDark,
                                                textListen = stringResource(R.string.action_tts_listen_urdu),
                                                textStop = stringResource(R.string.action_tts_stop_urdu),
                                                isRtl = true
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = targetName.meaningUrdu,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                color = colors.textSecondary,
                                                lineHeight = 28.sp,
                                                fontSize = 17.sp,
                                                textDirection = TextDirection.Rtl
                                            ),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(36.dp))
                }
            }
        }
    }
}

/**
 * Modern compact button for triggering offline Text-To-Speech for the Explanation section.
 */
@Composable
fun ExplanationTtsButton(
    isSpeaking: Boolean,
    onClick: () -> Unit,
    accentColor: Color,
    accentBackground: Color,
    isDark: Boolean,
    textListen: String,
    textStop: String,
    isRtl: Boolean = false,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ttsPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "speakerPulse"
    )

    val containerColor = if (isSpeaking) {
        if (isDark) accentColor.copy(alpha = 0.25f) else accentColor.copy(alpha = 0.18f)
    } else {
        accentBackground
    }

    val contentColor = accentColor

    val borderColor = if (isSpeaking) {
        accentColor.copy(alpha = 0.65f)
    } else {
        accentColor.copy(alpha = 0.25f)
    }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = containerColor,
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (isSpeaking) Icons.Filled.Stop else Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = if (isSpeaking) textStop else textListen,
                tint = contentColor,
                modifier = Modifier
                    .size(16.dp)
                    .then(if (isSpeaking) Modifier.scale(pulseScale) else Modifier)
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(
                text = if (isSpeaking) textStop else textListen,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = contentColor,
                    letterSpacing = if (isRtl) 0.sp else 0.4.sp
                )
            )
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

