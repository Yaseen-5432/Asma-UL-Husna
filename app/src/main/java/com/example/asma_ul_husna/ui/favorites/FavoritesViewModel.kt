package com.example.asma_ul_husna.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.asma_ul_husna.data.model.AsmaName
import com.example.asma_ul_husna.domain.repository.NamesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val favoriteNames: List<AsmaName> = emptyList(),
    val favoriteIds: Set<Int> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class FavoritesViewModel(
    private val repository: NamesRepository
) : ViewModel() {

    private val _allNames = MutableStateFlow<List<AsmaName>>(emptyList())
    private val _isLoading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)

    val uiState: StateFlow<FavoritesUiState> = combine(
        _allNames,
        repository.getFavoriteIds(),
        _isLoading,
        _error
    ) { allNames, favIds, loading, err ->
        val favoriteNamesList = allNames.filter { favIds.contains(it.id) }
        FavoritesUiState(
            favoriteNames = favoriteNamesList,
            favoriteIds = favIds,
            isLoading = loading,
            error = err
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FavoritesUiState(isLoading = true)
    )

    init {
        loadAllNames()
    }

    fun loadAllNames() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _allNames.value = repository.getAllNames()
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Failed to load names"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onFavoriteToggle(nameId: Int) {
        viewModelScope.launch {
            repository.toggleFavorite(nameId)
        }
    }
}

class FavoritesViewModelFactory(
    private val repository: NamesRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
