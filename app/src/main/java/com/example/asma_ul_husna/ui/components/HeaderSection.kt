package com.example.asma_ul_husna.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asma_ul_husna.R
import com.example.asma_ul_husna.ui.theme.appColors

/**
 * Modern header section for the Home screen.
 * Displays confident typography with generous whitespace and clear hierarchy.
 */
@Composable
fun HeaderSection(
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.appColors

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = stringResource(R.string.header_title),
            style = MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = colors.textOnBackground,
                letterSpacing = 0.sp
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.header_subtitle),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = colors.textOnBackgroundSecondary,
                fontWeight = FontWeight.Medium
            )
        )
    }
}
