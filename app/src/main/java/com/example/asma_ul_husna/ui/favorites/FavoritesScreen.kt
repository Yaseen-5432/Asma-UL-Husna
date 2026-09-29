package com.example.asma_ul_husna.ui.favorites

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.asma_ul_husna.R
import com.example.asma_ul_husna.ui.components.EmptyStateView
import com.example.asma_ul_husna.ui.components.ErrorStateView
import com.example.asma_ul_husna.ui.components.NameCard
import com.example.asma_ul_husna.ui.theme.appColors

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onNameClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    FavoritesContent(
        uiState = uiState,
        onNameClick = onNameClick,
        onFavoriteToggle = viewModel::onFavoriteToggle,
        onRetry = viewModel::loadAllNames,
        modifier = modifier
    )
}

@Composable
fun FavoritesContent(
    uiState: FavoritesUiState,
    onNameClick: (Int) -> Unit,
    onFavoriteToggle: (Int) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.appColors

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.mainBackground)
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                color = colors.lightGold,
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (uiState.error != null) {
            ErrorStateView(
                message = uiState.error,
                onRetry = onRetry,
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (uiState.favoriteNames.isEmpty()) {
            EmptyStateView(
                title = stringResource(R.string.favorites_empty_title),
                description = stringResource(R.string.favorites_empty_desc),
                icon = Icons.Outlined.FavoriteBorder,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 156.dp),
                contentPadding = PaddingValues(
                    start = 18.dp,
                    end = 18.dp,
                    top = 18.dp,
                    bottom = 96.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.favorites_title),
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = colors.textOnBackground
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${uiState.favoriteNames.size} saved",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = colors.textOnBackgroundSecondary
                                )
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (colors.isDark) colors.elevatedCardBackground else colors.primaryIndigo.copy(alpha = 0.35f),
                            border = if (colors.isDark) BorderStroke(1.dp, colors.borderSubtle) else null
                        ) {
                            Text(
                                text = "${uiState.favoriteNames.size} / 99",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = colors.lightGold
                                ),
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                items(
                    items = uiState.favoriteNames,
                    key = { it.id }
                ) { name ->
                    NameCard(
                        name = name,
                        isFavorite = true,
                        onCardClick = { onNameClick(name.id) },
                        onFavoriteToggle = { onFavoriteToggle(name.id) }
                    )
                }
            }
        }
    }
}
