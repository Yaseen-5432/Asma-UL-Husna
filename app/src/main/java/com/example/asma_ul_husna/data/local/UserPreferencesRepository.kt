package com.example.asma_ul_husna.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.asma_ul_husna.data.model.AppLanguage
import com.example.asma_ul_husna.data.model.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val FAVORITE_IDS = stringSetPreferencesKey("favorite_ids")
        val APP_THEME = stringPreferencesKey("app_theme")
        val APP_LANGUAGE = stringPreferencesKey("app_language")
    }

    val favoriteIds: Flow<Set<Int>> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val stringSet = preferences[PreferencesKeys.FAVORITE_IDS] ?: emptySet()
            stringSet.mapNotNull { it.toIntOrNull() }.toSet()
        }

    val appTheme: Flow<AppTheme> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val themeName = preferences[PreferencesKeys.APP_THEME] ?: AppTheme.SYSTEM.name
            try {
                AppTheme.valueOf(themeName)
            } catch (e: IllegalArgumentException) {
                AppTheme.SYSTEM
            }
        }

    val appLanguage: Flow<AppLanguage> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val langName = preferences[PreferencesKeys.APP_LANGUAGE] ?: AppLanguage.SYSTEM.name
            try {
                AppLanguage.valueOf(langName)
            } catch (e: IllegalArgumentException) {
                AppLanguage.SYSTEM
            }
        }

    suspend fun toggleFavorite(nameId: Int) {
        context.dataStore.edit { preferences ->
            val currentSet = preferences[PreferencesKeys.FAVORITE_IDS]?.toMutableSet() ?: mutableSetOf()
            val idString = nameId.toString()
            if (currentSet.contains(idString)) {
                currentSet.remove(idString)
            } else {
                currentSet.add(idString)
            }
            preferences[PreferencesKeys.FAVORITE_IDS] = currentSet
        }
    }

    suspend fun setFavorite(nameId: Int, isFavorite: Boolean) {
        context.dataStore.edit { preferences ->
            val currentSet = preferences[PreferencesKeys.FAVORITE_IDS]?.toMutableSet() ?: mutableSetOf()
            val idString = nameId.toString()
            if (isFavorite) {
                currentSet.add(idString)
            } else {
                currentSet.remove(idString)
            }
            preferences[PreferencesKeys.FAVORITE_IDS] = currentSet
        }
    }

    suspend fun setAppTheme(theme: AppTheme) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.APP_THEME] = theme.name
        }
    }

    suspend fun setAppLanguage(language: AppLanguage) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.APP_LANGUAGE] = language.name
        }
    }
}
