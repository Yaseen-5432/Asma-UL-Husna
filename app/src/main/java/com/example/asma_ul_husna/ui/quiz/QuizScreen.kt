package com.example.asma_ul_husna.ui.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asma_ul_husna.data.model.AppLanguage
import com.example.asma_ul_husna.data.model.QuizOption
import com.example.asma_ul_husna.data.model.QuizSessionQuestion
import com.example.asma_ul_husna.ui.theme.BrightGold
import com.example.asma_ul_husna.ui.theme.appColors
import kotlin.math.roundToInt

// =========================================================================
// Modern Vibrant Gradient Design Tokens
// =========================================================================
val GradientRadiantSunset = Brush.horizontalGradient(
    listOf(Color(0xFF8A2387), Color(0xFFE94057), Color(0xFFF27121))
)

val GradientElectricIndigo = Brush.linearGradient(
    listOf(Color(0xFF667EEA), Color(0xFF764BA2))
)

val GradientCyberCyan = Brush.horizontalGradient(
    listOf(Color(0xFF00F2FE), Color(0xFF4FACFE), Color(0xFF9B51E0))
)

val GradientGoldenGlow = Brush.horizontalGradient(
    listOf(Color(0xFFFFD200), Color(0xFFF7971E), Color(0xFFFF5722))
)

val GradientNeonEmerald = Brush.horizontalGradient(
    listOf(Color(0xFF11998E), Color(0xFF38EF7D))
)

val GradientVividCoral = Brush.horizontalGradient(
    listOf(Color(0xFFFF512F), Color(0xFFDD2476))
)

val GradientCardBorder = Brush.horizontalGradient(
    listOf(Color(0xFF9D4EDD), Color(0xFF00F2FE), Color(0xFFFF2E93))
)

val GradientHeaderProgressBar = Brush.horizontalGradient(
    listOf(Color(0xFF00F2FE), Color(0xFF4FACFE), Color(0xFF9D4EDD), Color(0xFFFF2E93), Color(0xFFFFD166))
)

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onNavigateToHome: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val colors = MaterialTheme.appColors

    BackHandler(enabled = uiState.screenState == QuizScreenState.IN_PROGRESS) {
        viewModel.showQuitDialog(true)
    }

    if (uiState.showQuitDialog) {
        QuizQuitDialog(
            onConfirmQuit = { viewModel.quitQuiz() },
            onDismiss = { viewModel.showQuitDialog(false) }
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colors.mainBackground
    ) {
        AnimatedContent(
            targetState = uiState.screenState,
            transitionSpec = {
                (fadeIn(animationSpec = tween(320)) + scaleIn(initialScale = 0.94f, animationSpec = tween(320)))
                    .togetherWith(fadeOut(animationSpec = tween(220)))
            },
            label = "QuizScreenStateTransition"
        ) { targetState ->
            when (targetState) {
                QuizScreenState.WELCOME -> {
                    QuizWelcomeContent(
                        uiState = uiState,
                        onStartQuiz = { viewModel.startQuiz() }
                    )
                }

                QuizScreenState.IN_PROGRESS -> {
                    QuizInProgressContent(
                        uiState = uiState,
                        onSelectOption = { viewModel.selectOption(it) },
                        onNextQuestion = { viewModel.nextQuestion() },
                        onQuitClick = { viewModel.showQuitDialog(true) }
                    )
                }

                QuizScreenState.RESULT -> {
                    QuizResultContent(
                        uiState = uiState,
                        onReviewClick = { viewModel.showReview() },
                        onRetryClick = { viewModel.startQuiz() },
                        onHomeClick = onNavigateToHome
                    )
                }

                QuizScreenState.REVIEW -> {
                    QuizReviewContent(
                        uiState = uiState,
                        onBackToResults = { viewModel.backToResults() },
                        onRetryClick = { viewModel.startQuiz() }
                    )
                }
            }
        }
    }
}

// =========================================================================
// 1. WELCOME SCREEN (Bright, Flashy, Animated Hero)
// =========================================================================
@Composable
private fun QuizWelcomeContent(
    uiState: QuizUiState,
    onStartQuiz: () -> Unit
) {
    val colors = MaterialTheme.appColors
    val infiniteTransition = rememberInfiniteTransition(label = "WelcomeAmbient")

    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val buttonScale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else pulseScale,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "ButtonScale"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Ambient colorful glowing blobs in background
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF8A2387).copy(alpha = 0.22f), Color.Transparent),
                    center = Offset(size.width * 0.2f, size.height * 0.15f),
                    radius = size.width * 0.6f
                ),
                center = Offset(size.width * 0.2f, size.height * 0.15f),
                radius = size.width * 0.6f
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFF00F2FE).copy(alpha = 0.18f), Color.Transparent),
                    center = Offset(size.width * 0.85f, size.height * 0.75f),
                    radius = size.width * 0.55f
                ),
                center = Offset(size.width * 0.85f, size.height * 0.75f),
                radius = size.width * 0.55f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 12.dp)
            ) {
                // Flashy Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(24.dp))
                        .background(GradientRadiantSunset)
                        .padding(1.5.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(if (colors.isDark) Color(0xFF140F2D) else Color(0xFF231942))
                            .padding(horizontal = 16.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFFFFD166),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "ASMA-UL-HUSNA QUIZ",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.3.sp
                            ),
                            color = Color(0xFFFFD166)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Knowledge Challenge",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.5).sp
                    ),
                    color = colors.textOnBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Reflect, test, and master the 99 Beautiful Names of Allah with an exciting interactive quiz.",
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = colors.textOnBackgroundSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Center Highlights
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                VibrantFeatureCard(
                    icon = Icons.Filled.School,
                    title = "10 Questions Per Session",
                    subtitle = "Curated from 297 comprehensive questions across all 99 Names.",
                    accentGradient = GradientCyberCyan
                )

                VibrantFeatureCard(
                    icon = Icons.Filled.WifiOff,
                    title = "100% Offline & Instant",
                    subtitle = "No internet required. Play anytime, anywhere with zero loading delay.",
                    accentGradient = GradientGoldenGlow
                )

                VibrantFeatureCard(
                    icon = Icons.Filled.EmojiEvents,
                    title = "Celebrations & Review",
                    subtitle = "Get animated feedback and review every answer with explanations.",
                    accentGradient = GradientRadiantSunset
                )
            }

            // Bottom Start Button
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .scale(buttonScale)
                        .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = Color(0xFFE94057))
                        .clip(RoundedCornerShape(20.dp))
                        .background(GradientRadiantSunset)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onStartQuiz
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(26.dp),
                            color = Color.White,
                            strokeWidth = 2.5.dp
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.PlayArrow,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "START QUIZ",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.1.sp
                                ),
                                color = Color.White
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun VibrantFeatureCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    accentGradient: Brush
) {
    val colors = MaterialTheme.appColors

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (colors.isDark) Color(0xFF191735) else Color(0xFF211A4F)
        ),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(accentGradient),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        lineHeight = 17.sp
                    ),
                    color = Color.White.copy(alpha = 0.75f)
                )
            }
        }
    }
}

// =========================================================================
// 2. IN-PROGRESS QUIZ (Animated Cards, Glowing Options, Gradient Progress)
// =========================================================================
@Composable
private fun QuizInProgressContent(
    uiState: QuizUiState,
    onSelectOption: (String) -> Unit,
    onNextQuestion: () -> Unit,
    onQuitClick: () -> Unit
) {
    val colors = MaterialTheme.appColors
    val currentSessionQ = uiState.currentQuestion ?: return
    val currentName = uiState.currentName

    val questionText = currentSessionQ.question.question.getText(uiState.language)
    val questionNumber = uiState.currentQuestionIndex + 1
    val totalCount = uiState.totalQuestions

    // Smooth animated progress bar with spring physics
    val animatedProgress by animateFloatAsState(
        targetValue = uiState.progress,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "AnimatedQuizProgress"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        // Top Header: Capsule Counter + Glowing Gradient Progress Bar + Quit Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(GradientCyberCyan)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Question $questionNumber / $totalCount",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Black
                            ),
                            color = Color.White
                        )
                    }

                    Text(
                        text = "${(uiState.progress * 100).roundToInt()}% Done",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = BrightGold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Custom Animated Gradient Progress Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.White.copy(alpha = 0.12f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedProgress)
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp))
                            .background(GradientHeaderProgressBar)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(
                onClick = onQuitClick,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.12f))
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Quit Quiz",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Name Context Glowing Pill
        if (currentName != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(GradientElectricIndigo)
                    .padding(1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (colors.isDark) Color(0xFF140F2D) else Color(0xFF231942))
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "#${currentName.id} ${currentName.transliteration}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                    Text(text = "•", color = BrightGold)
                    Text(
                        text = currentName.arabic,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = BrightGold
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        // Question Card with Animated Entrance
        AnimatedContent(
            targetState = currentSessionQ,
            transitionSpec = {
                (slideInHorizontally { width -> width / 3 } + fadeIn(animationSpec = tween(240)))
                    .togetherWith(slideOutHorizontally { width -> -width / 3 } + fadeOut(animationSpec = tween(180)))
            },
            label = "QuestionTransition"
        ) { sessionQ ->
            val qText = sessionQ.question.question.getText(uiState.language)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(GradientCardBorder)
                    .padding(1.5.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (colors.isDark) Color(0xFF191735) else Color(0xFF20174A)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = qText,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                lineHeight = 24.sp
                            ),
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Options List
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(currentSessionQ.shuffledOptions) { _, option ->
                val isSelected = uiState.selectedOptionId == option.id
                VibrantOptionCard(
                    option = option,
                    isSelected = isSelected,
                    language = uiState.language,
                    onSelect = { onSelectOption(option.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Next / Finish Button with Gradient & Press Animation
        val nextInteractionSource = remember { MutableInteractionSource() }
        val isNextPressed by nextInteractionSource.collectIsPressedAsState()
        val nextButtonScale by animateFloatAsState(
            targetValue = if (isNextPressed) 0.96f else 1f,
            animationSpec = spring(stiffness = Spring.StiffnessMedium),
            label = "NextButtonScale"
        )

        val isEnabled = uiState.isOptionSelected

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .scale(nextButtonScale)
                .shadow(
                    elevation = if (isEnabled) 10.dp else 0.dp,
                    shape = RoundedCornerShape(18.dp),
                    spotColor = Color(0xFFE94057)
                )
                .clip(RoundedCornerShape(18.dp))
                .background(
                    if (isEnabled) GradientRadiantSunset else Brush.horizontalGradient(
                        listOf(Color.White.copy(alpha = 0.15f), Color.White.copy(alpha = 0.15f))
                    )
                )
                .clickable(
                    interactionSource = nextInteractionSource,
                    indication = null,
                    enabled = isEnabled,
                    onClick = onNextQuestion
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = if (uiState.isLastQuestion) "FINISH QUIZ" else "NEXT QUESTION",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.8.sp
                    ),
                    color = if (isEnabled) Color.White else Color.White.copy(alpha = 0.45f)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = if (isEnabled) Color.White else Color.White.copy(alpha = 0.45f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun VibrantOptionCard(
    option: QuizOption,
    isSelected: Boolean,
    language: AppLanguage,
    onSelect: () -> Unit
) {
    val colors = MaterialTheme.appColors
    val optionText = option.text.getText(language)

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.025f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "OptionScale"
    )

    val backgroundGradient = if (isSelected) {
        Brush.horizontalGradient(listOf(Color(0xFF6A11CB).copy(alpha = 0.45f), Color(0xFF2575FC).copy(alpha = 0.45f)))
    } else {
        Brush.horizontalGradient(
            listOf(
                if (colors.isDark) Color(0xFF191735) else Color(0xFF20174A),
                if (colors.isDark) Color(0xFF191735) else Color(0xFF20174A)
            )
        )
    }

    val borderBrush = if (isSelected) GradientCardBorder else Brush.horizontalGradient(
        listOf(Color.White.copy(alpha = 0.15f), Color.White.copy(alpha = 0.15f))
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(18.dp))
            .background(borderBrush)
            .padding(if (isSelected) 2.dp else 1.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(backgroundGradient)
            .clickable { onSelect() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Letter Badge
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) GradientGoldenGlow else Brush.horizontalGradient(listOf(Color.White.copy(alpha = 0.12f), Color.White.copy(alpha = 0.12f)))),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option.id,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Black
                    ),
                    color = if (isSelected) Color(0xFF1A1235) else Color.White
                )
            }

            // Option Text
            Text(
                text = optionText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    lineHeight = 20.sp
                ),
                color = Color.White,
                modifier = Modifier.weight(1f)
            )

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF38EF7D)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = Color(0xFF0F3822),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// =========================================================================
// 3. RESULT SCREEN (MAJOR VISUAL UPGRADE: CELEBRATION vs CRYING)
// =========================================================================
@Composable
private fun QuizResultContent(
    uiState: QuizUiState,
    onReviewClick: () -> Unit,
    onRetryClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    val colors = MaterialTheme.appColors
    val score = uiState.score
    val total = uiState.totalQuestions
    val percentage = uiState.percentageScore
    val wrongCount = total - score

    // CELEBRATION CONDITION: percentage > 40
    // FAILURE CONDITION: percentage <= 40
    val isCelebration = percentage > 40

    // Smooth Animated Score Count Up (0 -> percentage)
    val animatedPercentage = remember { Animatable(0f) }
    val animatedScoreScale = remember { Animatable(0.6f) }

    LaunchedEffect(percentage) {
        animatedScoreScale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
        animatedPercentage.animateTo(
            targetValue = percentage.toFloat(),
            animationSpec = tween(durationMillis = 1300, easing = FastOutSlowInEasing)
        )
    }

    val displayedPercentage = animatedPercentage.value.roundToInt()

    val (titleMessage, subtitleMessage, badgeText) = if (isCelebration) {
        when {
            percentage >= 90 -> Triple("MashAllah! Outstanding!", "You have demonstrated mastery over the 99 Beautiful Names of Allah!", "★ PERFECT SCORE ★")
            percentage >= 70 -> Triple("Excellent Knowledge!", "Great achievement! You know the divine attributes very well.", "✦ EXCELLENT ✦")
            else -> Triple("Good Effort!", "You are on a noble learning journey. Keep reflecting and reviewing.", "✦ WELL DONE ✦")
        }
    } else {
        Triple("Needs More Practice", "Reflect upon the 99 Names, review the meanings, and try again!", "KEEP LEARNING")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // CONTINUOUS CELEBRATION ANIMATION FOR percentage > 40
        if (isCelebration) {
            ContinuousConfettiCelebration(
                modifier = Modifier.fillMaxSize(),
                particleCount = 50
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section Badge & Title
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isCelebration) GradientRadiantSunset else GradientElectricIndigo)
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.3.sp
                        ),
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = titleMessage,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black
                    ),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = subtitleMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }

            // Center Visual Focus: Score Card or Crying Face
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.graphicsLayer {
                    scaleX = animatedScoreScale.value
                    scaleY = animatedScoreScale.value
                }
            ) {
                if (!isCelebration) {
                    // CONTINUOUS ANIMATED CRYING FACE FOR percentage <= 40
                    PlayfulCryingFace(
                        modifier = Modifier.padding(bottom = 12.dp),
                        sizeDp = 115
                    )
                }

                // Glowing Score Card Container
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(28.dp))
                        .background(if (isCelebration) GradientCardBorder else GradientElectricIndigo)
                        .padding(2.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (colors.isDark) Color(0xFF191735) else Color(0xFF20174A)
                        ),
                        modifier = Modifier.width(300.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Circular Gradient Gauge
                            Box(
                                modifier = Modifier
                                    .size(130.dp)
                                    .clip(CircleShape)
                                    .background(if (isCelebration) GradientRadiantSunset else GradientElectricIndigo)
                                    .padding(4.dp)
                                    .clip(CircleShape)
                                    .background(if (colors.isDark) Color(0xFF120E29) else Color(0xFF1B143F)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$displayedPercentage%",
                                        style = MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.Black
                                        ),
                                        color = if (isCelebration) Color(0xFFFFD166) else Color(0xFF4FACFE)
                                    )
                                    Text(
                                        text = "$score / $total",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Breakdown Pills
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                // Correct Pill
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(GradientNeonEmerald)
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Check,
                                            contentDescription = null,
                                            tint = Color(0xFF06331A),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "$score Correct",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Black
                                            ),
                                            color = Color(0xFF06331A)
                                        )
                                    }
                                }

                                // Incorrect Pill
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(GradientVividCoral)
                                        .padding(horizontal = 14.dp, vertical = 8.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Close,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            text = "$wrongCount Wrong",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Black
                                            ),
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Actions
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Review Answers Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(GradientCardBorder)
                        .padding(1.5.dp)
                        .clip(RoundedCornerShape(17.dp))
                        .background(if (colors.isDark) Color(0xFF191735) else Color(0xFF20174A))
                        .clickable { onReviewClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = null,
                            tint = Color(0xFFFFD166),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "REVIEW ANSWERS",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            ),
                            color = Color.White
                        )
                    }
                }

                // Try Another Quiz Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .shadow(8.dp, RoundedCornerShape(18.dp), spotColor = Color(0xFFE94057))
                        .clip(RoundedCornerShape(18.dp))
                        .background(if (isCelebration) GradientRadiantSunset else GradientElectricIndigo)
                        .clickable { onRetryClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "TRY ANOTHER QUIZ",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.8.sp
                            ),
                            color = Color.White
                        )
                    }
                }

                TextButton(
                    onClick = onHomeClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Back to Home",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

// =========================================================================
// 4. REVIEW ANSWERS SCREEN (Detailed Glowing Review Cards)
// =========================================================================
@Composable
private fun QuizReviewContent(
    uiState: QuizUiState,
    onBackToResults: () -> Unit,
    onRetryClick: () -> Unit
) {
    val colors = MaterialTheme.appColors
    val userAnswers = uiState.userAnswers

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconButton(
                    onClick = onBackToResults,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.12f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = "Answer Review",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black
                    ),
                    color = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(GradientGoldenGlow)
                    .padding(horizontal = 12.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "${uiState.score}/${uiState.totalQuestions} Correct",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Black
                    ),
                    color = Color(0xFF1B143F)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Review Items List
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            itemsIndexed(userAnswers) { index, answer ->
                VibrantReviewItemCard(
                    index = index + 1,
                    userAnswer = answer,
                    language = uiState.language,
                    namesMap = uiState.namesMap
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Take Another Quiz Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .shadow(8.dp, RoundedCornerShape(18.dp), spotColor = Color(0xFFE94057))
                .clip(RoundedCornerShape(18.dp))
                .background(GradientRadiantSunset)
                .clickable { onRetryClick() },
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "TAKE ANOTHER QUIZ",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.8.sp
                    ),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun VibrantReviewItemCard(
    index: Int,
    userAnswer: QuizUserAnswer,
    language: AppLanguage,
    namesMap: Map<Int, com.example.asma_ul_husna.data.model.AsmaName>
) {
    val colors = MaterialTheme.appColors
    val sessionQ = userAnswer.sessionQuestion
    val originalQ = sessionQ.question
    val nameInfo = namesMap[originalQ.nameId]

    val questionText = originalQ.question.getText(language)
    val selectedOption = sessionQ.shuffledOptions.find { it.id == userAnswer.selectedOptionId }
    val correctOption = sessionQ.shuffledOptions.find { it.id == sessionQ.correctOptionId }

    val isCorrect = userAnswer.isCorrect

    val leftAccentGradient = if (isCorrect) GradientNeonEmerald else GradientVividCoral

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (colors.isDark) Color(0xFF191735) else Color(0xFF20174A)
        ),
        border = BorderStroke(1.dp, if (isCorrect) Color(0xFF38EF7D).copy(alpha = 0.4f) else Color(0xFFFF5252).copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header Row: Question # & Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Q$index",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Black
                        ),
                        color = BrightGold
                    )
                    if (nameInfo != null) {
                        Text(
                            text = "• #${nameInfo.id} ${nameInfo.transliteration} (${nameInfo.arabic})",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Color.White.copy(alpha = 0.75f)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(leftAccentGradient)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isCorrect) Icons.Filled.Check else Icons.Filled.Close,
                            contentDescription = null,
                            tint = if (isCorrect) Color(0xFF06331A) else Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = if (isCorrect) "Correct" else "Incorrect",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black
                            ),
                            color = if (isCorrect) Color(0xFF06331A) else Color.White
                        )
                    }
                }
            }

            // Question Text
            Text(
                text = questionText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )

            // Answers Breakdown
            if (isCorrect) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF38EF7D).copy(alpha = 0.15f))
                        .border(1.dp, Color(0xFF38EF7D).copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "Your Answer (Correct):",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black
                            ),
                            color = Color(0xFF38EF7D)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = selectedOption?.text?.getText(language) ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )
                    }
                }
            } else {
                // User Answer (Wrong)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFF5252).copy(alpha = 0.15f))
                        .border(1.dp, Color(0xFFFF5252).copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "Your Answer:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black
                            ),
                            color = Color(0xFFFF6E6E)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = selectedOption?.text?.getText(language) ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )
                    }
                }

                // Correct Answer
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF38EF7D).copy(alpha = 0.15f))
                        .border(1.dp, Color(0xFF38EF7D).copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                        .padding(10.dp)
                ) {
                    Column {
                        Text(
                            text = "Correct Answer:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Black
                            ),
                            color = Color(0xFF38EF7D)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = correctOption?.text?.getText(language) ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

// =========================================================================
// 5. QUIT DIALOG
// =========================================================================
@Composable
private fun QuizQuitDialog(
    onConfirmQuit: () -> Unit,
    onDismiss: () -> Unit
) {
    val colors = MaterialTheme.appColors

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Quit Quiz?",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )
        },
        text = {
            Text(
                text = "Your current quiz progress will be lost. Are you sure you want to leave?",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.75f)
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirmQuit,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5252))
            ) {
                Text(
                    text = "Quit",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Continue Quiz",
                    color = BrightGold,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        containerColor = if (colors.isDark) Color(0xFF191735) else Color(0xFF20174A),
        shape = RoundedCornerShape(22.dp)
    )
}
