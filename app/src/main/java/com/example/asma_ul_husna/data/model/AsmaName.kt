package com.example.asma_ul_husna.data.model

/**
 * Represents one of the 99 Beautiful Names of Allah (Asma-ul-Husna).
 * Matches the cross-platform unified schema v2.0.
 */
data class AsmaName(
    val id: Int,
    val arabic: String,
    val transliteration: String,
    val nameUrdu: String,
    val englishMeaning: String,
    val meaningUrdu: String,
    val explanation: String,
    val spiritualBenefits: String? = null,
    val quranicReference: String? = null,
    val audioFilename: String? = null,
    val category: String? = null
)
