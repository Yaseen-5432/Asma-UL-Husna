package com.example.asma_ul_husna.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.asma_ul_husna.audio.AudioPlayer
import com.example.asma_ul_husna.audio.RecitationState
import com.example.asma_ul_husna.data.model.AsmaName
import com.example.asma_ul_husna.domain.repository.NamesRepository
import com.example.asma_ul_husna.ui.components.NameFilterType
import com.example.asma_ul_husna.util.SearchUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

data class NamesUiState(
    val names: List<AsmaName> = emptyList(),
    val filteredNames: List<AsmaName> = emptyList(),
    val featuredName: AsmaName? = null,
    val searchQuery: String = "",
    val selectedFilter: NameFilterType = NameFilterType.ALL,
    val favoriteIds: Set<Int> = emptySet(),
    val recitationState: RecitationState = RecitationState(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val recitationCurrentNameId: Int?
        get() = recitationState.currentNameId

    val isRecitationPlaying: Boolean
        get() = recitationState.isPlaying

    val isRecitationPaused: Boolean
        get() = recitationState.isPaused

    val isRecitationLoading: Boolean
        get() = recitationState.isLoading
}

class HomeViewModel(
    private val repository: NamesRepository,
    private val audioPlayer: AudioPlayer
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedFilter = MutableStateFlow(NameFilterType.ALL)
    private val _names = MutableStateFlow<List<AsmaName>>(emptyList())
    private val _isLoading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)

    private val _filteredNames = combine(
        _names,
        _searchQuery,
        _selectedFilter,
        repository.getFavoriteIds()
    ) { names: List<AsmaName>, query: String, filter: NameFilterType, favorites: Set<Int> ->
        names.filter { name ->
            val matchesQuery = SearchUtils.matchesQuery(name, query)
            val matchesFilter = when (filter) {
                NameFilterType.ALL -> true
                NameFilterType.PART_1 -> name.id in 1..33
                NameFilterType.PART_2 -> name.id in 34..66
                NameFilterType.PART_3 -> name.id in 67..99
                NameFilterType.FAVORITES -> favorites.contains(name.id)
            }
            matchesQuery && matchesFilter
        }
    }

    val uiState: StateFlow<NamesUiState> = combine(
        _names,
        _filteredNames,
        combine(_searchQuery, _selectedFilter, repository.getFavoriteIds(), ::Triple),
        audioPlayer.recitationState,
        combine(_isLoading, _error, ::Pair)
    ) { names, filtered, (query, filter, favorites), recitation, (loading, err) ->
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val featured = if (names.isNotEmpty()) {
            val featuredId = ((dayOfYear - 1) % names.size) + 1
            names.firstOrNull { it.id == featuredId } ?: names.firstOrNull()
        } else null

        NamesUiState(
            names = names,
            filteredNames = filtered,
            featuredName = featured,
            searchQuery = query,
            selectedFilter = filter,
            favoriteIds = favorites,
            recitationState = recitation,
            isLoading = loading,
            error = err
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NamesUiState(isLoading = true)
    )

    init {
        loadNames()
    }

    fun loadNames() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val loadedNames = repository.getAllNames()
                _names.value = loadedNames
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Failed to load names"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFilterSelected(filter: NameFilterType) {
        _selectedFilter.value = filter
    }

    fun onFavoriteToggle(nameId: Int) {
        viewModelScope.launch {
            repository.toggleFavorite(nameId)
        }
    }

    fun toggleFullRecitation() {
        val state = audioPlayer.recitationState.value
        when {
            state.isPlaying -> audioPlayer.pauseFullRecitation()
            state.isPaused -> audioPlayer.resumeFullRecitation()
            else -> audioPlayer.playFullRecitation()
        }
    }

    fun stopFullRecitation() {
        audioPlayer.stopFullRecitation()
    }
}

class HomeViewModelFactory(
    private val repository: NamesRepository,
    private val audioPlayer: AudioPlayer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository, audioPlayer) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
