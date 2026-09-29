package com.example.asma_ul_husna.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.asma_ul_husna.data.model.AppTheme

/**
 * Unified application design tokens supporting both Light and Dark mode.
 */
@Immutable
data class AppColors(
    val isDark: Boolean,
    val mainBackground: Color,
    val secondaryBackground: Color,
    val cardBackground: Color,
    val elevatedCardBackground: Color,
    val elevatedCardGradientEnd: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val textOnBackground: Color,
    val textOnBackgroundSecondary: Color,
    val primaryIndigo: Color,
    val primaryGold: Color,
    val lightGold: Color,
    val borderSubtle: Color,
    val divider: Color,
    val searchBarBg: Color,
    val searchBarText: Color,
    val searchBarPlaceholder: Color,
    val searchBarIcon: Color,
    val bottomNavBg: Color,
    val bottomNavSelected: Color,
    val bottomNavUnselected: Color,
    val bottomNavIndicator: Color,
    val chipActiveBg: Color,
    val chipActiveText: Color,
    val chipInactiveBg: Color,
    val chipInactiveText: Color,
    val chipInactiveBorder: Color,
    val heartActive: Color,
    val heartInactive: Color,
    val cardAccent: (Int) -> CardAccentTheme
)

val LightAppColors = AppColors(
    isDark = false,
    mainBackground = DeepIndigo,
    secondaryBackground = DeepIndigoDark,
    cardBackground = CardWhite,
    elevatedCardBackground = CardWhite,
    elevatedCardGradientEnd = CardOffWhite,
    textPrimary = TextPrimary,
    textSecondary = TextSecondary,
    textMuted = TextMuted,
    textOnBackground = TextOnDark,
    textOnBackgroundSecondary = TextOnDarkSecondary,
    primaryIndigo = PrimaryPurple,
    primaryGold = IslamicGold,
    lightGold = BrightGold,
    borderSubtle = GlassBorder,
    divider = BorderNavy,
    searchBarBg = CardWhite,
    searchBarText = TextPrimary,
    searchBarPlaceholder = TextSecondary.copy(alpha = 0.8f),
    searchBarIcon = PrimaryPurple,
    bottomNavBg = DeepIndigoDark,
    bottomNavSelected = BrightGold,
    bottomNavUnselected = TextOnDarkSecondary.copy(alpha = 0.7f),
    bottomNavIndicator = PrimaryPurple,
    chipActiveBg = PrimaryPurple,
    chipActiveText = TextOnDark,
    chipInactiveBg = Color.White.copy(alpha = 0.12f),
    chipInactiveText = TextOnDarkSecondary,
    chipInactiveBorder = Color.White.copy(alpha = 0.2f),
    heartActive = FavoriteRed,
    heartInactive = TextSecondary.copy(alpha = 0.45f),
    cardAccent = ::getCardAccentTheme
)

val DarkAppColors = AppColors(
    isDark = true,
    mainBackground = DarkMainBackground,
    secondaryBackground = DarkSecondaryBackground,
    cardBackground = DarkCardBackground,
    elevatedCardBackground = DarkElevatedCardBackground,
    elevatedCardGradientEnd = DarkElevatedCardGradientEnd,
    textPrimary = DarkTextPrimary,
    textSecondary = DarkTextSecondary,
    textMuted = DarkTextMuted,
    textOnBackground = DarkTextPrimary,
    textOnBackgroundSecondary = DarkTextSecondary,
    primaryIndigo = DarkPrimaryIndigo,
    primaryGold = DarkPrimaryGold,
    lightGold = DarkLightGold,
    borderSubtle = DarkBorderSubtle,
    divider = DarkDivider,
    searchBarBg = DarkCardBackground,
    searchBarText = DarkTextPrimary,
    searchBarPlaceholder = DarkTextMuted,
    searchBarIcon = DarkTextSecondary,
    bottomNavBg = DarkSecondaryBackground,
    bottomNavSelected = DarkLightGold,
    bottomNavUnselected = DarkTextMuted,
    bottomNavIndicator = DarkPrimaryIndigo.copy(alpha = 0.35f),
    chipActiveBg = DarkPrimaryIndigo,
    chipActiveText = Color.White,
    chipInactiveBg = DarkCardBackground,
    chipInactiveText = DarkTextSecondary,
    chipInactiveBorder = DarkBorderSubtle,
    heartActive = DarkLightGold,
    heartInactive = DarkTextMuted,
    cardAccent = ::getDarkCardAccentTheme
)

val LocalAppColors = staticCompositionLocalOf { LightAppColors }

val MaterialTheme.appColors: AppColors
    @Composable
    get() = LocalAppColors.current

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimaryIndigo,
    onPrimary = Color.White,
    primaryContainer = DarkElevatedCardBackground,
    onPrimaryContainer = DarkLightGold,
    secondary = DarkLightGold,
    onSecondary = DarkSecondaryBackground,
    secondaryContainer = DarkCardBackground,
    onSecondaryContainer = DarkTextPrimary,
    background = DarkMainBackground,
    onBackground = DarkTextPrimary,
    surface = DarkCardBackground,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkElevatedCardBackground,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorderSubtle,
    outlineVariant = DarkLightGold.copy(alpha = 0.3f),
    error = DarkStatusError,
    onError = Color.White
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

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val appColors = if (darkTheme) DarkAppColors else LightAppColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val statusBarColor = if (darkTheme) DarkMainBackground.toArgb() else DeepIndigoDark.toArgb()
            val navBarColor = if (darkTheme) DarkSecondaryBackground.toArgb() else DeepIndigoDark.toArgb()
            window.statusBarColor = statusBarColor
            window.navigationBarColor = navBarColor
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = false
            insetsController.isAppearanceLightNavigationBars = false
        }
    }

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}