package com.example.asma_ul_husna.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.asma_ul_husna.data.model.AppLanguage
import com.example.asma_ul_husna.data.model.AppTheme
import com.example.asma_ul_husna.domain.repository.NamesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val currentTheme: AppTheme = AppTheme.SYSTEM,
    val currentLanguage: AppLanguage = AppLanguage.SYSTEM
)

class SettingsViewModel(
    private val repository: NamesRepository
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        repository.getThemePreference(),
        repository.getLanguagePreference()
    ) { theme, language ->
        SettingsUiState(
            currentTheme = theme,
            currentLanguage = language
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch {
            repository.setThemePreference(theme)
        }
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch {
            repository.setLanguagePreference(language)
        }
    }
}

class SettingsViewModelFactory(
    private val repository: NamesRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
