package com.example.asma_ul_husna.domain.repository

import com.example.asma_ul_husna.data.model.AppLanguage
import com.example.asma_ul_husna.data.model.AppTheme
import com.example.asma_ul_husna.data.model.AsmaName
import kotlinx.coroutines.flow.Flow

/**
 * Interface defining the operations for retrieving and managing Asma-ul-Husna data and user preferences.
 */
interface NamesRepository {
    suspend fun getAllNames(): List<AsmaName>
    suspend fun getNameById(id: Int): AsmaName?
    suspend fun getAudioUrl(id: Int): Result<String>
    fun getFavoriteIds(): Flow<Set<Int>>
    suspend fun toggleFavorite(id: Int)
    suspend fun setFavorite(id: Int, isFavorite: Boolean)
    fun getThemePreference(): Flow<AppTheme>
    suspend fun setThemePreference(theme: AppTheme)
    fun getLanguagePreference(): Flow<AppLanguage>
    suspend fun setLanguagePreference(language: AppLanguage)
}
