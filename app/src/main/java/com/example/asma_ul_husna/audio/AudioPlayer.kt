package com.example.asma_ul_husna.audio

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Represents the state of individual Name pronunciation audio playback.
 */
sealed interface AudioPlaybackState {
    object Idle : AudioPlaybackState
    data class Loading(val id: Int) : AudioPlaybackState
    data class Playing(val id: Int) : AudioPlaybackState
    data class Paused(val id: Int) : AudioPlaybackState
    data class Completed(val id: Int) : AudioPlaybackState
    data class Error(val id: Int?, val message: String) : AudioPlaybackState
}

val AudioPlaybackState.playingId: Int?
    get() = when (this) {
        is AudioPlaybackState.Loading -> id
        is AudioPlaybackState.Playing -> id
        is AudioPlaybackState.Paused -> id
        is AudioPlaybackState.Completed -> id
        is AudioPlaybackState.Error -> id
        AudioPlaybackState.Idle -> null
    }

val AudioPlaybackState.isPlaying: Boolean
    get() = this is AudioPlaybackState.Playing

val AudioPlaybackState.isLoading: Boolean
    get() = this is AudioPlaybackState.Loading

/**
 * Represents the state of continuous Full Recitation audio and timestamp synchronization.
 */
data class RecitationState(
    val isLoading: Boolean = false,
    val isPlaying: Boolean = false,
    val isPaused: Boolean = false,
    val currentNameId: Int? = null,
    val positionMs: Long = 0L,
    val durationMs: Long = 0L,
    val error: String? = null
)

/**
 * Clean abstraction for audio playback operations using Media3 ExoPlayer.
 * Decouples UI and ViewModels from underlying Media3/ExoPlayer implementations.
 */
interface AudioPlayer {
    val playbackState: StateFlow<AudioPlaybackState>
    val recitationState: StateFlow<RecitationState>

    // Individual Name playback
    fun play(id: Int, url: String)
    fun pause()
    fun resume()
    fun stop()

    // Full 99 Names recitation playback
    fun playFullRecitation()
    fun pauseFullRecitation()
    fun resumeFullRecitation()
    fun stopFullRecitation()

    fun release()
}

class AppAudioPlayer(context: Context) : AudioPlayer {

    private enum class PlaybackMode {
        IDLE,
        INDIVIDUAL,
        FULL_RECITATION
    }

    private val appContext = context.applicationContext
    private var exoPlayer: ExoPlayer? = null
    private var currentMode = PlaybackMode.IDLE

    private val playerScope = CoroutineScope(Dispatchers.Main + Job())
    private var tickerJob: Job? = null

    // Individual audio state
    private val _playbackState = MutableStateFlow<AudioPlaybackState>(AudioPlaybackState.Idle)
    override val playbackState: StateFlow<AudioPlaybackState> = _playbackState.asStateFlow()

    // Full recitation state
    private val _recitationState = MutableStateFlow(RecitationState())
    override val recitationState: StateFlow<RecitationState> = _recitationState.asStateFlow()

    private var currentPlayingId: Int? = null
    private var currentUrl: String? = null

    private val playerListener = object : Player.Listener {
        override fun onPlaybackStateChanged(state: Int) {
            when (state) {
                Player.STATE_IDLE -> {
                    if (currentMode == PlaybackMode.INDIVIDUAL && _playbackState.value !is AudioPlaybackState.Error) {
                        _playbackState.value = AudioPlaybackState.Idle
                    } else if (currentMode == PlaybackMode.FULL_RECITATION && _recitationState.value.error == null) {
                        _recitationState.value = RecitationState()
                    }
                }
                Player.STATE_BUFFERING -> {
                    if (currentMode == PlaybackMode.INDIVIDUAL) {
                        currentPlayingId?.let { id ->
                            _playbackState.value = AudioPlaybackState.Loading(id)
                        }
                    } else if (currentMode == PlaybackMode.FULL_RECITATION) {
                        _recitationState.value = _recitationState.value.copy(isLoading = true)
                    }
                }
                Player.STATE_READY -> {
                    if (currentMode == PlaybackMode.INDIVIDUAL) {
                        currentPlayingId?.let { id ->
                            if (exoPlayer?.isPlaying == true) {
                                _playbackState.value = AudioPlaybackState.Playing(id)
                            } else {
                                _playbackState.value = AudioPlaybackState.Paused(id)
                            }
                        }
                    } else if (currentMode == PlaybackMode.FULL_RECITATION) {
                        val isPlaying = exoPlayer?.isPlaying == true
                        val pos = exoPlayer?.currentPosition ?: 0L
                        val activeId = RecitationTiming.getActiveNameId(pos)
                        val duration = exoPlayer?.duration?.coerceAtLeast(0L) ?: 0L
                        _recitationState.value = _recitationState.value.copy(
                            isLoading = false,
                            isPlaying = isPlaying,
                            isPaused = !isPlaying,
                            currentNameId = activeId,
                            positionMs = pos,
                            durationMs = duration
                        )
                    }
                }
                Player.STATE_ENDED -> {
                    if (currentMode == PlaybackMode.INDIVIDUAL) {
                        val endedId = currentPlayingId ?: -1
                        currentPlayingId = null
                        currentUrl = null
                        currentMode = PlaybackMode.IDLE
                        _playbackState.value = AudioPlaybackState.Completed(endedId)
                    } else if (currentMode == PlaybackMode.FULL_RECITATION) {
                        tickerJob?.cancel()
                        currentMode = PlaybackMode.IDLE
                        _recitationState.value = RecitationState()
                    }
                }
            }
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            if (currentMode == PlaybackMode.INDIVIDUAL) {
                currentPlayingId?.let { id ->
                    if (isPlaying) {
                        _playbackState.value = AudioPlaybackState.Playing(id)
                    } else if (exoPlayer?.playbackState == Player.STATE_READY) {
                        _playbackState.value = AudioPlaybackState.Paused(id)
                    }
                }
            } else if (currentMode == PlaybackMode.FULL_RECITATION) {
                val pos = exoPlayer?.currentPosition ?: 0L
                val activeId = RecitationTiming.getActiveNameId(pos)
                if (isPlaying) {
                    _recitationState.value = _recitationState.value.copy(
                        isLoading = false,
                        isPlaying = true,
                        isPaused = false,
                        currentNameId = activeId,
                        positionMs = pos
                    )
                    startPositionTracker()
                } else if (exoPlayer?.playbackState == Player.STATE_READY) {
                    tickerJob?.cancel()
                    _recitationState.value = _recitationState.value.copy(
                        isLoading = false,
                        isPlaying = false,
                        isPaused = true,
                        currentNameId = activeId,
                        positionMs = pos
                    )
                }
            }
        }

        override fun onPlayerError(error: PlaybackException) {
            val message = when (error.errorCode) {
                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT ->
                    "Network connection failed. Please check internet connection."
                PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS,
                PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND ->
                    "Audio stream unavailable on server."
                else -> error.localizedMessage ?: "Audio playback error."
            }

            if (currentMode == PlaybackMode.INDIVIDUAL) {
                val errId = currentPlayingId
                currentPlayingId = null
                currentUrl = null
                currentMode = PlaybackMode.IDLE
                _playbackState.value = AudioPlaybackState.Error(errId, message)
            } else if (currentMode == PlaybackMode.FULL_RECITATION) {
                tickerJob?.cancel()
                currentMode = PlaybackMode.IDLE
                _recitationState.value = RecitationState(error = message)
            }
        }
    }

    private fun getOrCreatePlayer(): ExoPlayer {
        return exoPlayer ?: ExoPlayer.Builder(appContext).build().also {
            it.addListener(playerListener)
            exoPlayer = it
        }
    }

    private fun startPositionTracker() {
        tickerJob?.cancel()
        tickerJob = playerScope.launch {
            while (isActive) {
                val player = exoPlayer
                if (player != null && currentMode == PlaybackMode.FULL_RECITATION && player.isPlaying) {
                    val pos = player.currentPosition
                    val duration = player.duration.coerceAtLeast(0L)
                    val activeNameId = RecitationTiming.getActiveNameId(pos)

                    _recitationState.value = _recitationState.value.copy(
                        isPlaying = true,
                        isPaused = false,
                        isLoading = false,
                        currentNameId = activeNameId,
                        positionMs = pos,
                        durationMs = duration
                    )
                }
                delay(60L)
            }
        }
    }

    // ---------------------------------------------------------
    // Individual Audio Playback
    // ---------------------------------------------------------

    override fun play(id: Int, url: String) {
        if (url.isBlank()) {
            _playbackState.value = AudioPlaybackState.Error(id, "Audio URL is missing")
            return
        }

        // If full recitation is currently active, stop it cleanly
        if (currentMode == PlaybackMode.FULL_RECITATION) {
            tickerJob?.cancel()
            _recitationState.value = RecitationState()
        }

        currentMode = PlaybackMode.INDIVIDUAL
        val player = getOrCreatePlayer()

        // If same audio is paused and ready, resume playback
        if (currentPlayingId == id && currentUrl == url && player.playbackState == Player.STATE_READY) {
            resume()
            return
        }

        currentPlayingId = id
        currentUrl = url
        _playbackState.value = AudioPlaybackState.Loading(id)

        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    override fun pause() {
        if (currentMode != PlaybackMode.INDIVIDUAL) return
        exoPlayer?.pause()
        currentPlayingId?.let { id ->
            _playbackState.value = AudioPlaybackState.Paused(id)
        }
    }

    override fun resume() {
        if (currentMode != PlaybackMode.INDIVIDUAL) return
        val player = getOrCreatePlayer()
        player.play()
        currentPlayingId?.let { id ->
            _playbackState.value = AudioPlaybackState.Playing(id)
        }
    }

    override fun stop() {
        if (currentMode == PlaybackMode.INDIVIDUAL) {
            exoPlayer?.stop()
            currentPlayingId = null
            currentUrl = null
            currentMode = PlaybackMode.IDLE
            _playbackState.value = AudioPlaybackState.Idle
        }
    }

    // ---------------------------------------------------------
    // Full 99 Names Recitation Playback
    // ---------------------------------------------------------

    override fun playFullRecitation() {
        // If individual audio is active, stop it
        if (currentMode == PlaybackMode.INDIVIDUAL) {
            exoPlayer?.stop()
            currentPlayingId = null
            currentUrl = null
            _playbackState.value = AudioPlaybackState.Idle
        }

        val player = getOrCreatePlayer()

        // If recitation was paused and still ready in memory, resume
        if (currentMode == PlaybackMode.FULL_RECITATION && _recitationState.value.isPaused && player.playbackState == Player.STATE_READY) {
            resumeFullRecitation()
            return
        }

        currentMode = PlaybackMode.FULL_RECITATION
        _recitationState.value = RecitationState(isLoading = true)

        val mediaItem = MediaItem.fromUri(RecitationTiming.FULL_RECITATION_ASSET_URI)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
        startPositionTracker()
    }

    override fun pauseFullRecitation() {
        if (currentMode != PlaybackMode.FULL_RECITATION) return
        tickerJob?.cancel()
        exoPlayer?.pause()
        val pos = exoPlayer?.currentPosition ?: 0L
        val activeId = RecitationTiming.getActiveNameId(pos)
        _recitationState.value = _recitationState.value.copy(
            isPlaying = false,
            isPaused = true,
            isLoading = false,
            currentNameId = activeId,
            positionMs = pos
        )
    }

    override fun resumeFullRecitation() {
        if (currentMode != PlaybackMode.FULL_RECITATION) {
            playFullRecitation()
            return
        }
        val player = getOrCreatePlayer()
        player.play()
        startPositionTracker()
    }

    override fun stopFullRecitation() {
        if (currentMode == PlaybackMode.FULL_RECITATION) {
            tickerJob?.cancel()
            exoPlayer?.stop()
            currentMode = PlaybackMode.IDLE
            _recitationState.value = RecitationState()
        }
    }

    override fun release() {
        tickerJob?.cancel()
        playerScope.cancel()
        exoPlayer?.removeListener(playerListener)
        exoPlayer?.release()
        exoPlayer = null
        currentPlayingId = null
        currentUrl = null
        currentMode = PlaybackMode.IDLE
        _playbackState.value = AudioPlaybackState.Idle
        _recitationState.value = RecitationState()
    }
}
