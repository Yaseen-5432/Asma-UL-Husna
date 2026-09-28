package com.example.asma_ul_husna.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.asma_ul_husna.R
import com.example.asma_ul_husna.ui.components.AppSearchBar
import com.example.asma_ul_husna.ui.components.EmptyStateView
import com.example.asma_ul_husna.ui.components.ErrorStateView
import com.example.asma_ul_husna.ui.components.HeaderSection
import com.example.asma_ul_husna.ui.components.NameCard
import com.example.asma_ul_husna.ui.theme.DeepNavy
import com.example.asma_ul_husna.ui.theme.IslamicGold
import com.example.asma_ul_husna.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNameClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeScreenContent(
        uiState = uiState,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onNameClick = onNameClick,
        onFavoriteToggle = viewModel::onFavoriteToggle,
        onToggleRecitation = viewModel::toggleFullRecitation,
        onRetry = viewModel::loadNames,
        modifier = modifier
    )
}

@Composable
fun HomeScreenContent(
    uiState: NamesUiState,
    onSearchQueryChanged: (String) -> Unit,
    onNameClick: (Int) -> Unit,
    onFavoriteToggle: (Int) -> Unit,
    onToggleRecitation: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyGridState()

    // Synchronized auto-scroll: fires only when active Name ID changes (1-50)
    LaunchedEffect(uiState.recitationCurrentNameId) {
        val activeId = uiState.recitationCurrentNameId
        if (activeId != null) {
            val itemIndex = uiState.filteredNames.indexOfFirst { it.id == activeId }
            if (itemIndex != -1) {
                // Offset by 2 due to HeaderSection (0) and AppSearchBar (1)
                val targetGridIndex = itemIndex + 2
                gridState.animateScrollToItem(targetGridIndex)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepNavy)
    ) {
        if (uiState.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = IslamicGold,
                    strokeWidth = 3.dp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.loading_names),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        } else if (uiState.error != null) {
            ErrorStateView(
                message = stringResource(R.string.error_loading_names),
                onRetry = onRetry,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Adaptive(minSize = 150.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 96.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // Header Section with Full Recitation Controls
                item(span = { GridItemSpan(maxLineSpan) }) {
                    HeaderSection(
                        isPlaying = uiState.isRecitationPlaying,
                        isPaused = uiState.isRecitationPaused,
                        isLoading = uiState.isRecitationLoading,
                        onToggleRecitation = onToggleRecitation,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                // Search Bar
                item(span = { GridItemSpan(maxLineSpan) }) {
                    AppSearchBar(
                        query = uiState.searchQuery,
                        onQueryChange = onSearchQueryChanged,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Empty state if search returns nothing
                if (uiState.filteredNames.isEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        EmptyStateView(
                            title = stringResource(R.string.search_no_results),
                            description = stringResource(R.string.search_no_results_desc),
                            icon = Icons.Outlined.SearchOff,
                            modifier = Modifier.padding(vertical = 32.dp)
                        )
                    }
                } else {
                    // 99 Names Grid Cards with Highlighting
                    items(
                        items = uiState.filteredNames,
                        key = { it.id }
                    ) { name ->
                        NameCard(
                            name = name,
                            isFavorite = uiState.favoriteIds.contains(name.id),
                            isHighlighted = uiState.recitationCurrentNameId == name.id,
                            onCardClick = { onNameClick(name.id) },
                            onFavoriteToggle = { onFavoriteToggle(name.id) }
                        )
                    }
                }
            }
        }
    }
}
