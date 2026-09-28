package com.example.asma_ul_husna.data.repository

import com.example.asma_ul_husna.data.local.NamesLocalDataSource
import com.example.asma_ul_husna.data.local.UserPreferencesRepository
import com.example.asma_ul_husna.data.model.AppLanguage
import com.example.asma_ul_husna.data.model.AppTheme
import com.example.asma_ul_husna.data.model.AsmaName
import com.example.asma_ul_husna.data.remote.NamesRemoteDataSource
import com.example.asma_ul_husna.domain.repository.NamesRepository
import kotlinx.coroutines.flow.Flow

class NamesRepositoryImpl(
    private val localDataSource: NamesLocalDataSource,
    private val remoteDataSource: NamesRemoteDataSource,
    private val userPreferencesRepository: UserPreferencesRepository
) : NamesRepository {

    override suspend fun getAllNames(): List<AsmaName> {
        return localDataSource.getNames()
    }

    override suspend fun getNameById(id: Int): AsmaName? {
        return localDataSource.getNameById(id)
    }

    override suspend fun getAudioUrl(id: Int): Result<String> {
        val result = remoteDataSource.fetchAudioUrls()
        return result.mapCatching { map ->
            map[id] ?: throw NoSuchElementException("No audio URL available for name ID $id")
        }
    }

    override fun getFavoriteIds(): Flow<Set<Int>> {
        return userPreferencesRepository.favoriteIds
    }

    override suspend fun toggleFavorite(id: Int) {
        userPreferencesRepository.toggleFavorite(id)
    }

    override suspend fun setFavorite(id: Int, isFavorite: Boolean) {
        userPreferencesRepository.setFavorite(id, isFavorite)
    }

    override fun getThemePreference(): Flow<AppTheme> {
        return userPreferencesRepository.appTheme
    }

    override suspend fun setThemePreference(theme: AppTheme) {
        userPreferencesRepository.setAppTheme(theme)
    }

    override fun getLanguagePreference(): Flow<AppLanguage> {
        return userPreferencesRepository.appLanguage
    }

    override suspend fun setLanguagePreference(language: AppLanguage) {
        userPreferencesRepository.setAppLanguage(language)
    }
}
