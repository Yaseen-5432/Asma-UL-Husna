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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.asma_ul_husna.R
import com.example.asma_ul_husna.data.model.AsmaName
import com.example.asma_ul_husna.ui.components.AppSearchBar
import com.example.asma_ul_husna.ui.components.CategoryChipsRow
import com.example.asma_ul_husna.ui.components.EmptyStateView
import com.example.asma_ul_husna.ui.components.ErrorStateView
import com.example.asma_ul_husna.ui.components.FeaturedNameCard
import com.example.asma_ul_husna.ui.components.FullRecitationButton
import com.example.asma_ul_husna.ui.components.GiftBoxRevealOverlay
import com.example.asma_ul_husna.ui.components.HeaderSection
import com.example.asma_ul_husna.ui.components.HeroCard
import com.example.asma_ul_husna.ui.components.NameCard
import com.example.asma_ul_husna.ui.components.NameFilterType
import com.example.asma_ul_husna.ui.components.RecitationPresentationOverlay
import com.example.asma_ul_husna.ui.theme.BrightGold
import com.example.asma_ul_husna.ui.theme.DeepIndigo
import com.example.asma_ul_husna.ui.theme.TextOnDarkMuted
import com.example.asma_ul_husna.ui.theme.appColors

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
        onFilterSelected = viewModel::onFilterSelected,
        onNameClick = onNameClick,
        onFavoriteToggle = viewModel::onFavoriteToggle,
        onToggleRecitation = viewModel::toggleFullRecitation,
        onStopRecitation = viewModel::stopFullRecitation,
        onRetry = viewModel::loadNames,
        modifier = modifier
    )
}

@Composable
fun HomeScreenContent(
    uiState: NamesUiState,
    onSearchQueryChanged: (String) -> Unit,
    onFilterSelected: (NameFilterType) -> Unit,
    onNameClick: (Int) -> Unit,
    onFavoriteToggle: (Int) -> Unit,
    onToggleRecitation: () -> Unit,
    onStopRecitation: () -> Unit = {},
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyGridState()
    val activeId = uiState.recitationCurrentNameId
    val isRecitationActive = activeId != null

    var pendingGiftName by remember { mutableStateOf<AsmaName?>(null) }

    val handleNameCardClick: (Int) -> Unit = { nameId ->
        if (pendingGiftName == null) {
            val targetName = uiState.names.firstOrNull { it.id == nameId }
            if (targetName != null) {
                pendingGiftName = targetName
            } else {
                onNameClick(nameId)
            }
        }
    }

    // Find the currently recited Name object from the full dataset
    val currentActiveName = remember(activeId, uiState.names) {
        if (activeId != null) uiState.names.firstOrNull { it.id == activeId } else null
    }

    // Synchronized background grid centering during full recitation
    LaunchedEffect(activeId) {
        if (activeId != null) {
            val itemIndex = uiState.filteredNames.indexOfFirst { it.id == activeId }
            if (itemIndex != -1) {
                // Stable offset count for header items
                val headerOffsetCount = 6
                val targetGridIndex = itemIndex + headerOffsetCount

                val viewportHeight = gridState.layoutInfo.viewportSize.height
                val visibleItemHeight = gridState.layoutInfo.visibleItemsInfo
                    .firstOrNull { it.index == targetGridIndex }?.size?.height
                    ?: gridState.layoutInfo.visibleItemsInfo.firstOrNull { it.index >= headerOffsetCount }?.size?.height
                    ?: 360
                val centerOffset = -((viewportHeight - visibleItemHeight) / 2).coerceAtLeast(0)

                gridState.animateScrollToItem(
                    index = targetGridIndex,
                    scrollOffset = centerOffset
                )
            }
        }
    }

    val colors = MaterialTheme.appColors

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.mainBackground)
    ) {
        if (uiState.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    color = colors.lightGold,
                    strokeWidth = 3.dp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.loading_names),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.textMuted
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
                // 1. Confident Top Header (Stable Key)
                item(key = "header_section", span = { GridItemSpan(maxLineSpan) }) {
                    HeaderSection(
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                // 2. Modern Hero Card (Stable Key)
                item(key = "hero_card", span = { GridItemSpan(maxLineSpan) }) {
                    HeroCard(
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                // 3. Featured Name of the Day (Stable Key)
                uiState.featuredName?.let { featured ->
                    item(key = "featured_name_card", span = { GridItemSpan(maxLineSpan) }) {
                        FeaturedNameCard(
                            name = featured,
                            isFavorite = uiState.favoriteIds.contains(featured.id),
                            onNameClick = handleNameCardClick,
                            onFavoriteToggle = onFavoriteToggle,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }

                // 4. Compact Modern Full Recitation Button (Directly under Name of the Day - Stable Key)
                item(key = "full_recitation_button", span = { GridItemSpan(maxLineSpan) }) {
                    FullRecitationButton(
                        isPlaying = uiState.isRecitationPlaying,
                        isPaused = uiState.isRecitationPaused,
                        isLoading = uiState.isRecitationLoading,
                        onToggleRecitation = onToggleRecitation,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                // 5. Modern Search Bar (Stable Key - Never shifted/disposed during typing)
                item(key = "app_search_bar", span = { GridItemSpan(maxLineSpan) }) {
                    AppSearchBar(
                        query = uiState.searchQuery,
                        onQueryChange = onSearchQueryChanged,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                // 6. Modern Category / Range Filter Chips (Stable Key)
                item(key = "category_filter_chips", span = { GridItemSpan(maxLineSpan) }) {
                    CategoryChipsRow(
                        selectedFilter = uiState.selectedFilter,
                        onFilterSelected = onFilterSelected,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                // Empty State if search or filter returns nothing
                if (uiState.filteredNames.isEmpty()) {
                    item(key = "empty_state", span = { GridItemSpan(maxLineSpan) }) {
                        EmptyStateView(
                            title = stringResource(R.string.search_no_results),
                            description = stringResource(R.string.search_no_results_desc),
                            icon = Icons.Outlined.SearchOff,
                            modifier = Modifier.padding(vertical = 32.dp)
                        )
                    }
                } else {
                    // 99 Names Grid with Modern Cards
                    items(
                        items = uiState.filteredNames,
                        key = { "name_card_${it.id}" }
                    ) { name ->
                        val isHighlighted = activeId == name.id
                        val relativePosition = if (activeId != null) name.id - activeId else 0

                        NameCard(
                            name = name,
                            isFavorite = uiState.favoriteIds.contains(name.id),
                            isHighlighted = isHighlighted,
                            isRecitationActive = isRecitationActive,
                            isPlaying = uiState.isRecitationPlaying,
                            isPaused = uiState.isRecitationPaused,
                            relativePosition = relativePosition,
                            onCardClick = { handleNameCardClick(name.id) },
                            onFavoriteToggle = { onFavoriteToggle(name.id) }
                        )
                    }
                }
            }
        }

        // =========================================================================
        // Dedicated Floating 3D Centerpiece Presentation Layer for Full Recitation
        // =========================================================================
        RecitationPresentationOverlay(
            activeName = currentActiveName,
            isPlaying = uiState.isRecitationPlaying,
            isPaused = uiState.isRecitationPaused,
            onTogglePlayPause = onToggleRecitation,
            onDismiss = onStopRecitation,
            onNameClick = onNameClick
        )

        // =========================================================================
        // Gift-box Opening + Colorful Sprinkle Pop Navigation Overlay
        // =========================================================================
        GiftBoxRevealOverlay(
            name = pendingGiftName,
            onAnimationComplete = {
                val targetId = pendingGiftName?.id
                pendingGiftName = null
                if (targetId != null) {
                    onNameClick(targetId)
                }
            }
        )
    }
}
