package com.example.asma_ul_husna

import com.example.asma_ul_husna.ui.detail.ExplanationTtsLanguage
import com.example.asma_ul_husna.ui.detail.ExplanationTtsState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExplanationTtsStateTest {

    @Test
    fun `test default ExplanationTtsState is idle`() {
        val state = ExplanationTtsState()
        assertFalse(state.isSpeaking)
        assertNull(state.activeLanguage)
        assertNull(state.activeNameId)
    }

    @Test
    fun `test speaking English Explanation state`() {
        val state = ExplanationTtsState(
            isSpeaking = true,
            activeLanguage = ExplanationTtsLanguage.ENGLISH,
            activeNameId = 1
        )
        assertTrue(state.isSpeaking)
        assertEquals(ExplanationTtsLanguage.ENGLISH, state.activeLanguage)
        assertEquals(1, state.activeNameId)
    }

    @Test
    fun `test speaking Urdu Explanation state`() {
        val state = ExplanationTtsState(
            isSpeaking = true,
            activeLanguage = ExplanationTtsLanguage.URDU,
            activeNameId = 42
        )
        assertTrue(state.isSpeaking)
        assertEquals(ExplanationTtsLanguage.URDU, state.activeLanguage)
        assertEquals(42, state.activeNameId)
    }

    @Test
    fun `test resetting ExplanationTtsState to idle`() {
        var state = ExplanationTtsState(
            isSpeaking = true,
            activeLanguage = ExplanationTtsLanguage.ENGLISH,
            activeNameId = 5
        )
        assertTrue(state.isSpeaking)

        // Reset
        state = ExplanationTtsState()
        assertFalse(state.isSpeaking)
        assertNull(state.activeLanguage)
        assertNull(state.activeNameId)
    }
}
