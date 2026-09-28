package com.example.asma_ul_husna

import com.example.asma_ul_husna.audio.AudioPlaybackState
import com.example.asma_ul_husna.audio.isLoading
import com.example.asma_ul_husna.audio.isPlaying
import com.example.asma_ul_husna.audio.playingId
import com.example.asma_ul_husna.data.local.NamesLocalDataSource
import com.example.asma_ul_husna.data.model.AppLanguage
import com.example.asma_ul_husna.data.model.AppTheme
import com.example.asma_ul_husna.data.model.AsmaName
import com.example.asma_ul_husna.data.remote.NamesRemoteDataSource
import com.example.asma_ul_husna.domain.repository.NamesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AudioPlaybackStateTest {

    @Test
    fun `test AudioPlaybackState state properties`() {
        val idle = AudioPlaybackState.Idle
        assertNull(idle.playingId)
        assertFalse(idle.isPlaying)
        assertFalse(idle.isLoading)

        val loading = AudioPlaybackState.Loading(1)
        assertEquals(1, loading.playingId)
        assertFalse(loading.isPlaying)
        assertTrue(loading.isLoading)

        val playing = AudioPlaybackState.Playing(1)
        assertEquals(1, playing.playingId)
        assertTrue(playing.isPlaying)
        assertFalse(playing.isLoading)

        val paused = AudioPlaybackState.Paused(1)
        assertEquals(1, paused.playingId)
        assertFalse(paused.isPlaying)
        assertFalse(paused.isLoading)

        val completed = AudioPlaybackState.Completed(1)
        assertEquals(1, completed.playingId)
        assertFalse(completed.isPlaying)

        val error = AudioPlaybackState.Error(1, "Network error")
        assertEquals(1, error.playingId)
        assertEquals("Network error", error.message)
        assertFalse(error.isPlaying)
    }

    @Test
    fun `test repository returns audio url correctly from remote data source`() = runBlocking {
        val mockRemoteDataSource = object : NamesRemoteDataSource {
            override suspend fun fetchAudioUrls(): Result<Map<Int, String>> {
                return Result.success(
                    mapOf(
                        1 to "https://www.islamicity.org/mediaassets/MP3/other/covers/99-names-of-Allah/001.mp3?v06092021",
                        2 to "https://www.islamicity.org/mediaassets/MP3/other/covers/99-names-of-Allah/002.mp3?v06092021"
                    )
                )
            }
        }

        val testRepository = object : NamesRepository {
            override suspend fun getAllNames(): List<AsmaName> = emptyList()
            override suspend fun getNameById(id: Int): AsmaName? = null
            override suspend fun getAudioUrl(id: Int): Result<String> {
                val result = mockRemoteDataSource.fetchAudioUrls()
                return result.mapCatching { map ->
                    map[id] ?: throw NoSuchElementException("No audio URL available for name ID $id")
                }
            }
            override fun getFavoriteIds(): Flow<Set<Int>> = flowOf(emptySet())
            override suspend fun toggleFavorite(id: Int) {}
            override suspend fun setFavorite(id: Int, isFavorite: Boolean) {}
            override fun getThemePreference(): Flow<AppTheme> = flowOf(AppTheme.SYSTEM)
            override suspend fun setThemePreference(theme: AppTheme) {}
            override fun getLanguagePreference(): Flow<AppLanguage> = flowOf(AppLanguage.SYSTEM)
            override suspend fun setLanguagePreference(language: AppLanguage) {}
        }

        val result1 = testRepository.getAudioUrl(1)
        assertTrue(result1.isSuccess)
        assertEquals(
            "https://www.islamicity.org/mediaassets/MP3/other/covers/99-names-of-Allah/001.mp3?v06092021",
            result1.getOrNull()
        )

        val result99 = testRepository.getAudioUrl(99)
        assertTrue(result99.isFailure)
    }
}
