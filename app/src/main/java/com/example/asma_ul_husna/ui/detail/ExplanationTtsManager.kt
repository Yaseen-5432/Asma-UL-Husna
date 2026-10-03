package com.example.asma_ul_husna.ui.detail

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import com.example.asma_ul_husna.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

/**
 * Language options for Explanation Text-To-Speech.
 */
enum class ExplanationTtsLanguage {
    ENGLISH,
    URDU
}

/**
 * UI State of the Explanation TTS.
 */
data class ExplanationTtsState(
    val isSpeaking: Boolean = false,
    val activeLanguage: ExplanationTtsLanguage? = null,
    val activeNameId: Int? = null
)

/**
 * Offline Text-To-Speech manager for the Explanation section using Android's native TextToSpeech engine.
 * Lifecycle-safe, queue-safe (QUEUE_FLUSH), and completely offline.
 */
class ExplanationTtsManager(
    private val context: Context,
    private val onMessage: (String) -> Unit
) {
    private val mainHandler = Handler(Looper.getMainLooper())
    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private var pendingSpeech: (() -> Unit)? = null

    private val _state = MutableStateFlow(ExplanationTtsState())
    val state: StateFlow<ExplanationTtsState> = _state.asStateFlow()

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        // Keep current speaking state active
                    }

                    override fun onDone(utteranceId: String?) {
                        mainHandler.post {
                            _state.value = ExplanationTtsState(
                                isSpeaking = false,
                                activeLanguage = null,
                                activeNameId = null
                            )
                        }
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        mainHandler.post {
                            _state.value = ExplanationTtsState(
                                isSpeaking = false,
                                activeLanguage = null,
                                activeNameId = null
                            )
                        }
                    }

                    override fun onError(utteranceId: String?, errorCode: Int) {
                        mainHandler.post {
                            _state.value = ExplanationTtsState(
                                isSpeaking = false,
                                activeLanguage = null,
                                activeNameId = null
                            )
                        }
                    }
                })

                // Execute any queued request that occurred during initialization
                mainHandler.post {
                    pendingSpeech?.invoke()
                    pendingSpeech = null
                }
            } else {
                isInitialized = false
                mainHandler.post {
                    _state.value = ExplanationTtsState()
                }
            }
        }
    }

    /**
     * Speaks or stops the explanation for the given Name and language.
     * If the same explanation is already being spoken, stops speech (toggle behavior).
     * If another explanation is requested, flushes the queue and speaks the new text.
     */
    fun speak(
        nameId: Int,
        text: String,
        language: ExplanationTtsLanguage,
        onPlaybackStarted: () -> Unit = {}
    ) {
        val currentState = _state.value
        // Toggle behavior: if already speaking this exact explanation, stop it.
        if (currentState.isSpeaking && currentState.activeNameId == nameId && currentState.activeLanguage == language) {
            stop()
            return
        }

        if (text.isBlank()) return

        if (!isInitialized) {
            pendingSpeech = {
                speak(nameId, text, language, onPlaybackStarted)
            }
            return
        }

        val engine = tts ?: return

        // Set and verify locale support
        when (language) {
            ExplanationTtsLanguage.ENGLISH -> {
                val locale = Locale.US
                var result = engine.setLanguage(locale)
                if (result < TextToSpeech.LANG_AVAILABLE) {
                    result = engine.setLanguage(Locale.ENGLISH)
                }
                if (result < TextToSpeech.LANG_AVAILABLE) {
                    mainHandler.post {
                        onMessage(context.getString(R.string.tts_english_unavailable))
                    }
                    return
                }
            }
            ExplanationTtsLanguage.URDU -> {
                val urduLocale = Locale.forLanguageTag("ur-PK")
                var result = engine.setLanguage(urduLocale)
                if (result < TextToSpeech.LANG_AVAILABLE) {
                    result = engine.setLanguage(Locale.forLanguageTag("ur"))
                }
                if (result < TextToSpeech.LANG_AVAILABLE) {
                    mainHandler.post {
                        onMessage(context.getString(R.string.tts_urdu_unavailable))
                    }
                    return
                }
            }
        }

        // Notify caller that TTS is starting (e.g. to pause conflicting audio)
        onPlaybackStarted()

        _state.value = ExplanationTtsState(
            isSpeaking = true,
            activeLanguage = language,
            activeNameId = nameId
        )

        val utteranceId = "explanation_${nameId}_${language.name}"
        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
        }

        val speakResult = engine.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
        if (speakResult != TextToSpeech.SUCCESS) {
            _state.value = ExplanationTtsState(
                isSpeaking = false,
                activeLanguage = null,
                activeNameId = null
            )
        }
    }

    /**
     * Immediately stops speech and resets UI state.
     */
    fun stop() {
        pendingSpeech = null
        try {
            if (isInitialized) {
                tts?.stop()
            }
        } catch (_: Exception) {
            // Ignore any cleanup errors
        }
        _state.value = ExplanationTtsState(
            isSpeaking = false,
            activeLanguage = null,
            activeNameId = null
        )
    }

    /**
     * Releases TextToSpeech resources when leaving the screen.
     */
    fun release() {
        pendingSpeech = null
        try {
            if (isInitialized) {
                tts?.stop()
                tts?.shutdown()
            }
        } catch (_: Exception) {
            // Ignore any cleanup errors
        }
        tts = null
        isInitialized = false
        _state.value = ExplanationTtsState(
            isSpeaking = false,
            activeLanguage = null,
            activeNameId = null
        )
    }
}
