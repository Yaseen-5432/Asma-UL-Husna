package com.example.asma_ul_husna.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.asma_ul_husna.data.model.AppTheme

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryPurple,
    onPrimary = TextWhite,
    primaryContainer = DeepIndigoSurface,
    onPrimaryContainer = PaleGold,
    secondary = BrightGold,
    onSecondary = DeepIndigo,
    secondaryContainer = DeepIndigoSurface,
    onSecondaryContainer = TextWhite,
    background = DeepIndigo,
    onBackground = TextWhite,
    surface = CardWhite,
    onSurface = TextPrimary,
    surfaceVariant = CardOffWhite,
    onSurfaceVariant = TextSecondary,
    outline = BorderNavy,
    outlineVariant = GoldBorder,
    error = FavoriteRed,
    onError = TextWhite
)

private val LightColorScheme = darkColorScheme(
    primary = PrimaryPurple,
    onPrimary = TextWhite,
    primaryContainer = DeepIndigoSurface,
    onPrimaryContainer = PaleGold,
    secondary = BrightGold,
    onSecondary = DeepIndigo,
    secondaryContainer = DeepIndigoSurface,
    onSecondaryContainer = TextWhite,
    background = DeepIndigo,
    onBackground = TextWhite,
    surface = CardWhite,
    onSurface = TextPrimary,
    surfaceVariant = CardOffWhite,
    onSurfaceVariant = TextSecondary,
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

    // App identity uses deep indigo (#1E174F), primary purple, gold accents, and clean white cards
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DeepIndigoDark.toArgb()
            window.navigationBarColor = DeepIndigoDark.toArgb()
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