package com.example.asma_ul_husna.domain.ai

import com.example.asma_ul_husna.data.model.AsmaName

/**
 * Domain interface for Phase 2 AI Divine Reflection Assistant.
 * Decouples presentation and domain logic from any specific AI provider (Groq, on-device LLM, etc.).
 */
interface ReflectionAssistant {
    suspend fun generateReflection(
        name: AsmaName,
        prompt: String
    ): Result<String>
}
