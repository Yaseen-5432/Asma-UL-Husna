package com.example.asma_ul_husna.ui.theme

import androidx.compose.ui.graphics.Color

// =========================================================================
// Core Modern Palette (Deep Indigo, Primary Purple, Warm Gold & Card White)
// =========================================================================

// Backgrounds
val DeepIndigo = Color(0xFF1E174F)
val DeepIndigoDark = Color(0xFF16103D)
val DeepIndigoSurface = Color(0xFF282064)
val DeepNavy = DeepIndigo // Alias for backward compatibility

// Brand Purple & Indigo
val PrimaryPurple = Color(0xFF4338A8)
val PrimaryPurpleLight = Color(0xFF6356DE)
val PrimaryPurpleDark = Color(0xFF312782)
val PurpleAccentContainer = Color(0xFFEDE9FE)

// Islamic Gold Identity (#D4AF37, #F4C95D, #FAFAF7)
val IslamicGold = Color(0xFFD4AF37)
val SoftGold = Color(0xFFD4AF37)
val BrightGold = Color(0xFFF4C95D)
val WarmGold = Color(0xFFE5B842)
val PaleGold = Color(0xFFFBF4D8)
val DarkGold = Color(0xFFA67C1E)
val GoldGradientStart = Color(0xFFF4C95D)
val GoldGradientEnd = Color(0xFFC89928)
val GoldBorder = Color(0x33D4AF37)
val GoldContainer = Color(0xFFFFF9E6)

// Surfaces & Cards
val CardWhite = Color(0xFFFFFFFF)
val CardOffWhite = Color(0xFFFAFAF7)
val CardSurfaceSubtle = Color(0xFFF5F6FA)
val SurfaceNavy = Color(0xFF282064)
val CardNavy = Color(0xFF282064)
val BorderNavy = Color(0x264338A8)

// Translucent & Ambient Overlays
val GlassSurface = Color(0xFFFFFFFF) // Modern clean white card surface
val GlassSurfaceElevated = Color(0xFFFAFAF7)
val GlassSurfaceHighlight = Color(0xFFFFFFFF)
val GlassBorder = Color(0x1F202124) // Soft shadow-border
val GlassBorderSubtle = Color(0x0F202124)

// Text Colors
val TextWhite = Color(0xFFFAFAF7)
val TextPrimary = Color(0xFF1F2937) // Deep Charcoal #202124 for White Cards
val TextSecondary = Color(0xFF6B7280) // Modern Neutral Gray for White Cards
val TextMuted = Color(0xFF9CA3AF)
val TextMutedLight = Color(0xFFD1D5DB)
val TextNavy = Color(0xFF1E174F)
val TextOnDark = Color(0xFFFAFAF7)
val TextOnDarkSecondary = Color(0xFFC7D2FE)
val TextOnDarkMuted = Color(0xFF94A3B8)

// Status & Indicators
val FavoriteRed = Color(0xFFEF4444)
val FavoriteRedSoft = Color(0xFFFEE2E2)
val FavoriteRedActive = Color(0xFFDC2626)

// =========================================================================
// Controlled Card Multi-Color Accent System
// =========================================================================
val AccentGold = Color(0xFFD4AF37)
val AccentGoldBg = Color(0xFFFFFBEB)

val AccentPurple = Color(0xFF4338A8)
val AccentPurpleBg = Color(0xFFEEF2FF)

val AccentTeal = Color(0xFF0D9488)
val AccentTealBg = Color(0xFFCCFBF1)

val AccentBlue = Color(0xFF2563EB)
val AccentBlueBg = Color(0xFFEFF6FF)

val AccentCoral = Color(0xFFE11D48)
val AccentCoralBg = Color(0xFFFFE4E6)

val AccentEmerald = Color(0xFF059669)
val AccentEmeraldBg = Color(0xFFD1FAE5)

val AccentAmber = Color(0xFFD97706)
val AccentAmberBg = Color(0xFFFEF3C7)

val AccentIndigo = Color(0xFF4F46E5)
val AccentIndigoBg = Color(0xFFEDE9FE)

data class CardAccentTheme(
    val primary: Color,
    val background: Color,
    val border: Color
)

/**
 * Returns a deterministic accent palette for a Name card based on its ID.
 */
fun getCardAccentTheme(nameId: Int): CardAccentTheme {
    val accents = listOf(
        CardAccentTheme(AccentGold, AccentGoldBg, AccentGold.copy(alpha = 0.35f)),
        CardAccentTheme(AccentPurple, AccentPurpleBg, AccentPurple.copy(alpha = 0.25f)),
        CardAccentTheme(AccentTeal, AccentTealBg, AccentTeal.copy(alpha = 0.25f)),
        CardAccentTheme(AccentBlue, AccentBlueBg, AccentBlue.copy(alpha = 0.25f)),
        CardAccentTheme(AccentCoral, AccentCoralBg, AccentCoral.copy(alpha = 0.25f)),
        CardAccentTheme(AccentEmerald, AccentEmeraldBg, AccentEmerald.copy(alpha = 0.25f)),
        CardAccentTheme(AccentAmber, AccentAmberBg, AccentAmber.copy(alpha = 0.25f)),
        CardAccentTheme(AccentIndigo, AccentIndigoBg, AccentIndigo.copy(alpha = 0.25f))
    )
    val index = (nameId - 1).coerceAtLeast(0) % accents.size
    return accents[index]
}

// Light Theme Palette (Deep Indigo & Card White visual identity)
val LightBackground = DeepIndigo
val LightSurface = CardWhite
val LightSurfaceVariant = CardOffWhite
val LightBorder = GlassBorder
val LightPrimary = PrimaryPurple