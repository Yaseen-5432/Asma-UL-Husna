package com.example.asma_ul_husna.domain.usecase

import com.example.asma_ul_husna.data.model.QuizSessionQuestion
import com.example.asma_ul_husna.domain.repository.QuizRepository

/**
 * Use case to generate a randomized 10-question quiz session.
 */
class GetQuizSessionUseCase(
    private val quizRepository: QuizRepository
) {
    suspend operator fun invoke(questionCount: Int = 10): List<QuizSessionQuestion> {
        return quizRepository.generateQuizSession(questionCount)
    }
}
