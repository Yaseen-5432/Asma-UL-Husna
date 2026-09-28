package com.example.asma_ul_husna.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.asma_ul_husna.data.model.AppTheme

private val DarkColorScheme = darkColorScheme(
    primary = IslamicGold,
    onPrimary = DeepNavy,
    primaryContainer = GoldContainer,
    onPrimaryContainer = PaleGold,
    secondary = BrightGold,
    onSecondary = DeepNavy,
    secondaryContainer = GlassSurfaceElevated,
    onSecondaryContainer = TextWhite,
    background = DeepNavy,
    onBackground = TextWhite,
    surface = GlassSurface,
    onSurface = TextWhite,
    surfaceVariant = GlassSurfaceElevated,
    onSurfaceVariant = TextMuted,
    outline = BorderNavy,
    outlineVariant = GoldBorder,
    error = FavoriteRed,
    onError = TextWhite
)

private val LightColorScheme = darkColorScheme(
    primary = IslamicGold,
    onPrimary = DeepNavy,
    primaryContainer = GoldContainer,
    onPrimaryContainer = PaleGold,
    secondary = BrightGold,
    onSecondary = DeepNavy,
    secondaryContainer = GlassSurfaceElevated,
    onSecondaryContainer = TextWhite,
    background = DeepNavy,
    onBackground = TextWhite,
    surface = GlassSurface,
    onSurface = TextWhite,
    surfaceVariant = GlassSurfaceElevated,
    onSurfaceVariant = TextMuted,
    outline = BorderNavy,
    outlineVariant = GoldBorder,
    error = FavoriteRed,
    onError = TextWhite
)

@Composable
fun AsmaulHusnaTheme(
    appTheme: AppTheme = AppTheme.SYSTEM,
    content: @Composable () -> Unit
) {
    val darkTheme = when (appTheme) {
        AppTheme.SYSTEM -> isSystemInDarkTheme()
        AppTheme.DARK -> true
        AppTheme.LIGHT -> false
    }

    // App identity requires deep navy (#0d1b2a), gold (#d4af37), and glassmorphism across all screens
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DeepNavy.toArgb()
            window.navigationBarColor = DeepNavy.toArgb()
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = false
            insetsController.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}