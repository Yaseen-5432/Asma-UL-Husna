package com.example.asma_ul_husna

import com.example.asma_ul_husna.data.model.AsmaName
import com.example.asma_ul_husna.util.SearchUtils
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchUtilsTest {

    private val sampleName = AsmaName(
        id = 1,
        arabic = "الرَّحْمَنُ",
        transliteration = "Ar-Rahman",
        nameUrdu = "نہایت مہربان",
        englishMeaning = "The Most Gracious",
        meaningUrdu = "وہ ذات جس کی رحمت تمام مخلوقات کو اس دنیا میں عام ہے۔",
        explanation = "He whose endless mercy encompasses all creation in this worldly life.",
        audioFilename = "audio_1.mp3"
    )

    @Test
    fun `empty query matches all names`() {
        assertTrue(SearchUtils.matchesQuery(sampleName, ""))
        assertTrue(SearchUtils.matchesQuery(sampleName, "   "))
    }

    @Test
    fun `exact and partial transliteration matches case-insensitively`() {
        assertTrue(SearchUtils.matchesQuery(sampleName, "Rahman"))
        assertTrue(SearchUtils.matchesQuery(sampleName, "rahman"))
        assertTrue(SearchUtils.matchesQuery(sampleName, "Ar-Rahman"))
        assertTrue(SearchUtils.matchesQuery(sampleName, "ar-rahman"))
        assertTrue(SearchUtils.matchesQuery(sampleName, "Ar Rahman"))
        assertTrue(SearchUtils.matchesQuery(sampleName, "ArRahman"))
    }

    @Test
    fun `english meaning and explanation matching`() {
        assertTrue(SearchUtils.matchesQuery(sampleName, "Gracious"))
        assertTrue(SearchUtils.matchesQuery(sampleName, "gracious"))
        assertTrue(SearchUtils.matchesQuery(sampleName, "mercy"))
    }

    @Test
    fun `urdu meaning and explanation matching`() {
        assertTrue(SearchUtils.matchesQuery(sampleName, "مہربان"))
        assertTrue(SearchUtils.matchesQuery(sampleName, "رحمت"))
        assertTrue(SearchUtils.matchesQuery(sampleName, "مخلوقات"))
    }

    @Test
    fun `arabic matching with and without diacritics`() {
        // With Tashkeel
        assertTrue(SearchUtils.matchesQuery(sampleName, "الرَّحْمَنُ"))
        // Without Tashkeel
        assertTrue(SearchUtils.matchesQuery(sampleName, "الرحمن"))
        assertTrue(SearchUtils.matchesQuery(sampleName, "رحمن"))
    }

    @Test
    fun `id number matching`() {
        assertTrue(SearchUtils.matchesQuery(sampleName, "1"))
        assertFalse(SearchUtils.matchesQuery(sampleName, "2"))
    }

    @Test
    fun `non matching query returns false`() {
        assertFalse(SearchUtils.matchesQuery(sampleName, "Quddus"))
        assertFalse(SearchUtils.matchesQuery(sampleName, "قدوس"))
        assertFalse(SearchUtils.matchesQuery(sampleName, "Unrelated"))
    }
}
