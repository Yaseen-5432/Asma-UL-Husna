package com.example.asma_ul_husna

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class NamesJsonParsingTest {

    @Test
    fun `verify names json contains exactly 99 unique valid records conforming to schema v2`() {
        val file = File("src/main/assets/names.json")
        assertTrue("names.json file must exist in assets", file.exists())

        val jsonContent = file.readText()
        assertFalse("names.json must not be empty", jsonContent.isBlank())

        // Extract individual JSON objects from the array
        val objectRegex = Regex("""\{[^{}]*\}""", RegexOption.DOT_MATCHES_ALL)
        val objects = objectRegex.findAll(jsonContent).map { it.value }.toList()

        assertEquals("There must be exactly 99 Names of Allah", 99, objects.size)

        val seenIds = mutableSetOf<Int>()

        for ((index, objStr) in objects.withIndex()) {
            val idMatch = Regex(""""id"\s*:\s*(\d+)""").find(objStr)
            assertNotNull("Must contain id field", idMatch)
            val id = idMatch!!.groupValues[1].toInt()

            val arabicMatch = Regex(""""arabic"\s*:\s*"([^"]+)"""").find(objStr)
            assertNotNull("Must contain arabic field", arabicMatch)
            val arabic = arabicMatch!!.groupValues[1]

            val transliterationMatch = Regex(""""transliteration"\s*:\s*"([^"]+)"""").find(objStr)
            assertNotNull("Must contain transliteration field", transliterationMatch)
            val transliteration = transliterationMatch!!.groupValues[1]

            val nameUrduMatch = Regex(""""name_urdu"\s*:\s*"([^"]+)"""").find(objStr)
            assertNotNull("Must contain name_urdu field", nameUrduMatch)
            val nameUrdu = nameUrduMatch!!.groupValues[1]

            val englishMeaningMatch = Regex(""""english_meaning"\s*:\s*"([^"]+)"""").find(objStr)
            assertNotNull("Must contain english_meaning field", englishMeaningMatch)
            val englishMeaning = englishMeaningMatch!!.groupValues[1]

            val meaningUrduMatch = Regex(""""meaning_urdu"\s*:\s*"([^"]+)"""").find(objStr)
            assertNotNull("Must contain meaning_urdu field", meaningUrduMatch)
            val meaningUrdu = meaningUrduMatch!!.groupValues[1]

            val explanationMatch = Regex(""""explanation"\s*:\s*"([^"]+)"""").find(objStr)
            assertNotNull("Must contain explanation field", explanationMatch)
            val explanation = explanationMatch!!.groupValues[1]

            val audioFilenameMatch = Regex(""""audio_filename"\s*:\s*"([^"]+)"""").find(objStr)
            assertNotNull("Must contain audio_filename field", audioFilenameMatch)
            val audioFilename = audioFilenameMatch!!.groupValues[1]

            // Verify sequential ID
            assertEquals("ID should match index + 1", index + 1, id)
            assertFalse("Duplicate ID found: $id", seenIds.contains(id))
            seenIds.add(id)

            // Verify non-empty required fields
            assertTrue("Arabic must not be blank for ID $id", arabic.isNotBlank())
            assertTrue("Transliteration must not be blank for ID $id", transliteration.isNotBlank())
            assertTrue("Urdu name must not be blank for ID $id", nameUrdu.isNotBlank())
            assertTrue("English meaning must not be blank for ID $id", englishMeaning.isNotBlank())
            assertTrue("Urdu meaning must not be blank for ID $id", meaningUrdu.isNotBlank())
            assertTrue("Explanation must not be blank for ID $id", explanation.isNotBlank())

            // Verify audio filename format
            assertEquals("audio_${id}.mp3", audioFilename)

            // Verify presence of schema fields
            assertTrue(objStr.contains("\"spiritual_benefits\""))
            assertTrue(objStr.contains("\"quranic_reference\""))
            assertTrue(objStr.contains("\"category\""))
        }

        assertEquals("Total unique IDs must be 99", 99, seenIds.size)
        assertTrue("First ID must be 1", seenIds.contains(1))
        assertTrue("Last ID must be 99", seenIds.contains(99))
    }
}
