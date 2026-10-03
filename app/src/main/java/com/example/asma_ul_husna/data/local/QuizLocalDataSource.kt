package com.example.asma_ul_husna.data.local

import com.example.asma_ul_husna.data.model.QuizQuestion

/**
 * Local data source contract for loading quiz questions.
 */
interface QuizLocalDataSource {
    suspend fun getAllQuestions(): List<QuizQuestion>
    suspend fun getQuestionsByNameId(nameId: Int): List<QuizQuestion>
}
