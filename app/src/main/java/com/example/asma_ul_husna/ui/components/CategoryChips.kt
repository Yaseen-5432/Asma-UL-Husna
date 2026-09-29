package com.example.asma_ul_husna.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.asma_ul_husna.R
import com.example.asma_ul_husna.ui.theme.CardWhite
import com.example.asma_ul_husna.ui.theme.PrimaryPurple
import com.example.asma_ul_husna.ui.theme.TextOnDark
import com.example.asma_ul_husna.ui.theme.TextOnDarkSecondary

enum class NameFilterType(val titleRes: Int) {
    ALL(R.string.filter_all),
    PART_1(R.string.filter_part1),
    PART_2(R.string.filter_part2),
    PART_3(R.string.filter_part3),
    FAVORITES(R.string.filter_favorites)
}

/**
 * Modern horizontal category / range filter chips row.
 */
@Composable
fun CategoryChipsRow(
    selectedFilter: NameFilterType,
    onFilterSelected: (NameFilterType) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        NameFilterType.values().forEach { filter ->
            val isSelected = filter == selectedFilter

            val chipBgColor by animateColorAsState(
                targetValue = if (isSelected) PrimaryPurple else Color.White.copy(alpha = 0.12f),
                label = "chipBgColor"
            )

            val chipTextColor by animateColorAsState(
                targetValue = if (isSelected) TextOnDark else TextOnDarkSecondary,
                label = "chipTextColor"
            )

            Surface(
                onClick = { onFilterSelected(filter) },
                shape = RoundedCornerShape(20.dp),
                color = chipBgColor,
                border = if (isSelected) {
                    BorderStroke(1.dp, PrimaryPurple)
                } else {
                    BorderStroke(0.5.dp, Color.White.copy(alpha = 0.2f))
                }
            ) {
                Text(
                    text = stringResource(filter.titleRes),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = chipTextColor
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}
