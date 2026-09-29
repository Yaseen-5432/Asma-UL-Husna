package com.example.asma_ul_husna.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.SettingsBrightness
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asma_ul_husna.R
import com.example.asma_ul_husna.data.model.AppLanguage
import com.example.asma_ul_husna.data.model.AppTheme
import com.example.asma_ul_husna.ui.theme.AccentGold
import com.example.asma_ul_husna.ui.theme.AccentGoldBg
import com.example.asma_ul_husna.ui.theme.AccentPurple
import com.example.asma_ul_husna.ui.theme.AccentPurpleBg
import com.example.asma_ul_husna.ui.theme.CardWhite
import com.example.asma_ul_husna.ui.theme.DeepIndigo
import com.example.asma_ul_husna.ui.theme.IslamicGold
import com.example.asma_ul_husna.ui.theme.PrimaryPurple
import com.example.asma_ul_husna.ui.theme.TextOnDark
import com.example.asma_ul_husna.ui.theme.TextPrimary
import com.example.asma_ul_husna.ui.theme.TextSecondary

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    SettingsContent(
        uiState = uiState,
        onThemeSelected = viewModel::setTheme,
        onLanguageSelected = viewModel::setLanguage,
        modifier = modifier
    )
}

@Composable
fun SettingsContent(
    uiState: SettingsUiState,
    onThemeSelected: (AppTheme) -> Unit,
    onLanguageSelected: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepIndigo)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .padding(bottom = 80.dp)
        ) {
            // Header
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = TextOnDark
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 1. Appearance / Theme Card (Modern White Card)
            SettingsCard(
                title = stringResource(R.string.settings_theme_section),
                icon = Icons.Outlined.Palette,
                iconTint = PrimaryPurple,
                iconBg = AccentPurpleBg
            ) {
                Column {
                    ThemeOptionRow(
                        title = stringResource(R.string.settings_theme_system),
                        icon = Icons.Outlined.SettingsBrightness,
                        selected = uiState.currentTheme == AppTheme.SYSTEM,
                        onClick = { onThemeSelected(AppTheme.SYSTEM) }
                    )

                    ThemeOptionRow(
                        title = stringResource(R.string.settings_theme_dark),
                        icon = Icons.Outlined.DarkMode,
                        selected = uiState.currentTheme == AppTheme.DARK,
                        onClick = { onThemeSelected(AppTheme.DARK) }
                    )

                    ThemeOptionRow(
                        title = stringResource(R.string.settings_theme_light),
                        icon = Icons.Outlined.LightMode,
                        selected = uiState.currentTheme == AppTheme.LIGHT,
                        onClick = { onThemeSelected(AppTheme.LIGHT) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 2. Language Card (Modern White Card)
            SettingsCard(
                title = stringResource(R.string.settings_language_section),
                icon = Icons.Outlined.Language,
                iconTint = IslamicGold,
                iconBg = AccentGoldBg
            ) {
                Column {
                    LanguageOptionRow(
                        title = stringResource(R.string.settings_theme_system),
                        subtitle = "System Default",
                        selected = uiState.currentLanguage == AppLanguage.SYSTEM,
                        onClick = { onLanguageSelected(AppLanguage.SYSTEM) }
                    )

                    LanguageOptionRow(
                        title = stringResource(R.string.settings_language_en),
                        subtitle = "English",
                        selected = uiState.currentLanguage == AppLanguage.ENGLISH,
                        onClick = { onLanguageSelected(AppLanguage.ENGLISH) }
                    )

                    LanguageOptionRow(
                        title = stringResource(R.string.settings_language_ur),
                        subtitle = "اردو",
                        selected = uiState.currentLanguage == AppLanguage.URDU,
                        onClick = { onLanguageSelected(AppLanguage.URDU) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 3. About Card (Modern White Card)
            SettingsCard(
                title = stringResource(R.string.settings_about_section),
                icon = Icons.Outlined.Info,
                iconTint = PrimaryPurple,
                iconBg = AccentPurpleBg
            ) {
                Column(
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.settings_about_description),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondary,
                            lineHeight = 22.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.settings_version_label),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = TextPrimary
                            )
                        )

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AccentPurpleBg
                        ) {
                            Text(
                                text = "v${stringResource(R.string.settings_version_value)}",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryPurple
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsCard(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    content: @Composable () -> Unit
) {
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
                .padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(iconBg, shape = RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            content()
        }
    }
}

@Composable
fun ThemeOptionRow(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) PrimaryPurple else TextSecondary,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) TextPrimary else TextSecondary
            ),
            modifier = Modifier.weight(1f)
        )

        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = PrimaryPurple,
                unselectedColor = TextSecondary.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
fun LanguageOptionRow(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    color = if (selected) TextPrimary else TextSecondary
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary.copy(alpha = 0.7f)
                )
            )
        }

        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = PrimaryPurple,
                unselectedColor = TextSecondary.copy(alpha = 0.5f)
            )
        )
    }
}
