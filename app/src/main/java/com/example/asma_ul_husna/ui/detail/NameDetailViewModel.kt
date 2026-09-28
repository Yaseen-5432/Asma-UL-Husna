package com.example.asma_ul_husna.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.asma_ul_husna.audio.AudioPlaybackState
import com.example.asma_ul_husna.audio.AudioPlayer
import com.example.asma_ul_husna.data.model.AsmaName
import com.example.asma_ul_husna.domain.repository.NamesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private data class AudioUiState(
    val playbackState: AudioPlaybackState = AudioPlaybackState.Idle,
    val isAudioUrlLoading: Boolean = false,
    val audioError: String? = null
)

data class NameDetailUiState(
    val name: AsmaName? = null,
    val isFavorite: Boolean = false,
    val playbackState: AudioPlaybackState = AudioPlaybackState.Idle,
    val isAudioUrlLoading: Boolean = false,
    val audioError: String? = null,
    val totalCount: Int = 99,
    val isLoading: Boolean = true,
    val error: String? = null
)

class NameDetailViewModel(
    private val nameId: Int,
    private val repository: NamesRepository,
    private val audioPlayer: AudioPlayer
) : ViewModel() {

    private val _currentId = MutableStateFlow(nameId)
    private val _name = MutableStateFlow<AsmaName?>(null)
    private val _isLoading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)
    private val _isAudioUrlLoading = MutableStateFlow(false)
    private val _audioError = MutableStateFlow<String?>(null)

    private val _audioState = combine(
        audioPlayer.playbackState,
        _isAudioUrlLoading,
        _audioError
    ) { playbackState, audioLoading, audioErr ->
        AudioUiState(
            playbackState = playbackState,
            isAudioUrlLoading = audioLoading,
            audioError = audioErr
        )
    }

    val uiState: StateFlow<NameDetailUiState> = combine(
        _name,
        repository.getFavoriteIds(),
        _audioState,
        _isLoading,
        _error
    ) { name, favorites, audio, loading, err ->
        NameDetailUiState(
            name = name,
            isFavorite = name != null && favorites.contains(name.id),
            playbackState = audio.playbackState,
            isAudioUrlLoading = audio.isAudioUrlLoading,
            audioError = audio.audioError,
            isLoading = loading,
            error = err
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NameDetailUiState(isLoading = true)
    )

    init {
        loadName(_currentId.value)
    }

    fun loadName(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            _audioError.value = null
            audioPlayer.stop()
            try {
                val foundName = repository.getNameById(id)
                if (foundName != null) {
                    _currentId.value = id
                    _name.value = foundName
                } else {
                    _error.value = "Name not found"
                }
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Error loading details"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite() {
        val currentName = _name.value ?: return
        viewModelScope.launch {
            repository.toggleFavorite(currentName.id)
        }
    }

    fun playAudio() {
        val currentName = _name.value ?: return
        val currentAudioState = audioPlayer.playbackState.value

        // If this specific name is already playing, pause it
        if (currentAudioState is AudioPlaybackState.Playing && currentAudioState.id == currentName.id) {
            audioPlayer.pause()
            return
        }

        // If this specific name is paused, resume it
        if (currentAudioState is AudioPlaybackState.Paused && currentAudioState.id == currentName.id) {
            audioPlayer.resume()
            return
        }

        // Otherwise fetch remote audio_url from API and play
        viewModelScope.launch {
            _isAudioUrlLoading.value = true
            _audioError.value = null

            val result = repository.getAudioUrl(currentName.id)
            result.fold(
                onSuccess = { audioUrl ->
                    _isAudioUrlLoading.value = false
                    audioPlayer.play(currentName.id, audioUrl)
                },
                onFailure = { throwable ->
                    _isAudioUrlLoading.value = false
                    _audioError.value = throwable.localizedMessage ?: "Unable to load audio from server"
                }
            )
        }
    }

    fun pauseAudio() {
        audioPlayer.pause()
    }

    fun clearAudioError() {
        _audioError.value = null
    }

    fun navigateToNext() {
        val current = _currentId.value
        val nextId = if (current >= 99) 1 else current + 1
        loadName(nextId)
    }

    fun navigateToPrevious() {
        val current = _currentId.value
        val prevId = if (current <= 1) 99 else current - 1
        loadName(prevId)
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.stop()
    }
}

class NameDetailViewModelFactory(
    private val nameId: Int,
    private val repository: NamesRepository,
    private val audioPlayer: AudioPlayer
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NameDetailViewModel::class.java)) {
            return NameDetailViewModel(nameId, repository, audioPlayer) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
