package com.example.asma_ul_husna.domain.ai

import com.example.asma_ul_husna.data.model.AsmaName

/**
 * Domain interface for Phase 2 Personalized Daily Reflection & Dua Generator.
 */
interface DailyDuaGenerator {
    suspend fun generateDailyDua(
        theme: String,
        relevantNames: List<AsmaName>
    ): Result<String>
}
