package com.example.asma_ul_husna.util

import com.example.asma_ul_husna.data.model.AsmaName

object SearchUtils {

    // Regex matching Arabic diacritics / tashkeel
    private val TASHKEEL_REGEX = Regex("[\\u064B-\\u0652\\u0670\\u0640]")

    /**
     * Strips Arabic diacritical marks (harakat/tashkeel) for flexible Arabic search matching.
     */
    fun normalizeArabic(text: String): String {
        return text.replace(TASHKEEL_REGEX, "")
            .replace("أ", "ا")
            .replace("إ", "ا")
            .replace("آ", "ا")
            .replace("ة", "ه")
            .replace("ى", "ي")
            .trim()
    }

    /**
     * Normalizes English/Latin text by removing special punctuation like hyphens and apostrophes.
     */
    fun normalizeLatin(text: String): String {
        return text.lowercase()
            .replace("-", "")
            .replace("'", "")
            .replace("`", "")
            .replace("’", "")
            .replace(" ", "")
            .trim()
    }

    /**
     * Checks if an AsmaName matches the search query across all standardized fields.
     */
    fun matchesQuery(name: AsmaName, query: String): Boolean {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isEmpty()) return true

        // Match by number/ID
        if (trimmedQuery.toIntOrNull() == name.id) return true

        val lowerQuery = trimmedQuery.lowercase()
        val normalizedLatinQuery = normalizeLatin(trimmedQuery)
        val normalizedArabicQuery = normalizeArabic(trimmedQuery)

        // Transliteration matching
        if (name.transliteration.lowercase().contains(lowerQuery) ||
            normalizeLatin(name.transliteration).contains(normalizedLatinQuery)
        ) {
            return true
        }

        // English Meaning & Explanation matching
        if (name.englishMeaning.lowercase().contains(lowerQuery) ||
            name.explanation.lowercase().contains(lowerQuery)
        ) {
            return true
        }

        // Urdu Name & Meaning matching
        if (name.nameUrdu.contains(trimmedQuery) ||
            name.meaningUrdu.contains(trimmedQuery)
        ) {
            return true
        }

        // Category matching (if present)
        if (name.category?.lowercase()?.contains(lowerQuery) == true) {
            return true
        }

        // Arabic matching (exact and normalized)
        if (name.arabic.contains(trimmedQuery)) return true

        val normalizedArabicName = normalizeArabic(name.arabic)
        if (normalizedArabicName.contains(normalizedArabicQuery) ||
            normalizedArabicName.contains(trimmedQuery)
        ) {
            return true
        }

        return false
    }
}
