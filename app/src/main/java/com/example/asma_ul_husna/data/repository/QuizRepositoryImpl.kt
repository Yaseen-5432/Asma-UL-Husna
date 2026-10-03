package com.example.asma_ul_husna.data.repository

import com.example.asma_ul_husna.data.local.QuizLocalDataSource
import com.example.asma_ul_husna.data.local.UserPreferencesRepository
import com.example.asma_ul_husna.data.model.AppLanguage
import com.example.asma_ul_husna.data.model.QuizOption
import com.example.asma_ul_husna.data.model.QuizQuestion
import com.example.asma_ul_husna.data.model.QuizSessionQuestion
import com.example.asma_ul_husna.domain.repository.QuizRepository
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of QuizRepository managing quiz generation, shuffling, and preferences.
 */
class QuizRepositoryImpl(
    private val localDataSource: QuizLocalDataSource,
    private val userPreferencesRepository: UserPreferencesRepository
) : QuizRepository {

    override suspend fun getAllQuestions(): List<QuizQuestion> {
        return localDataSource.getAllQuestions()
    }

    override suspend fun getQuestionsByNameId(nameId: Int): List<QuizQuestion> {
        return localDataSource.getQuestionsByNameId(nameId)
    }

    override suspend fun generateQuizSession(questionCount: Int): List<QuizSessionQuestion> {
        val allQuestions = localDataSource.getAllQuestions()
        val count = questionCount.coerceIn(1, allQuestions.size)
        val selectedQuestions = allQuestions.shuffled().take(count)
        val optionLetters = listOf("A", "B", "C", "D")

        return selectedQuestions.map { originalQuestion ->
            // Shuffle the 4 options
            val shuffledOriginalOptions = originalQuestion.options.shuffled()
            
            // Re-assign display IDs "A", "B", "C", "D" to the shuffled positions
            val remappedOptions = shuffledOriginalOptions.mapIndexed { index, option ->
                QuizOption(
                    id = optionLetters[index],
                    text = option.text
                )
            }

            // Identify which new letter corresponds to the original correct option
            val correctOriginalId = originalQuestion.correctOption
            val correctShuffledIndex = shuffledOriginalOptions.indexOfFirst { it.id == correctOriginalId }
            val correctOptionLetter = if (correctShuffledIndex >= 0) {
                optionLetters[correctShuffledIndex]
            } else {
                optionLetters[0]
            }

            QuizSessionQuestion(
                question = originalQuestion,
                shuffledOptions = remappedOptions,
                correctOptionId = correctOptionLetter
            )
        }
    }

    override fun getLanguagePreference(): Flow<AppLanguage> {
        return userPreferencesRepository.appLanguage
    }
}
