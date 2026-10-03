package com.example.asma_ul_husna.domain.repository

import com.example.asma_ul_husna.data.model.AppLanguage
import com.example.asma_ul_husna.data.model.QuizQuestion
import com.example.asma_ul_husna.data.model.QuizSessionQuestion
import kotlinx.coroutines.flow.Flow

/**
 * Domain repository contract for Quiz operations.
 */
interface QuizRepository {
    suspend fun getAllQuestions(): List<QuizQuestion>
    suspend fun getQuestionsByNameId(nameId: Int): List<QuizQuestion>
    suspend fun generateQuizSession(questionCount: Int = 10): List<QuizSessionQuestion>
    fun getLanguagePreference(): Flow<AppLanguage>
}
