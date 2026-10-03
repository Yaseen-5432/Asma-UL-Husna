package com.example.asma_ul_husna.data.model

/**
 * Type of quiz question based on Name attributes.
 */
enum class QuizQuestionType {
    MEANING,
    EXPLANATION,
    UNDERSTANDING
}

/**
 * Holds localized text for English and Urdu languages.
 */
data class QuizLocalizedText(
    val en: String,
    val ur: String
) {
    fun getText(language: AppLanguage): String {
        return when (language) {
            AppLanguage.URDU -> ur
            else -> en
        }
    }
}

/**
 * Represents a single answer option for a question.
 */
data class QuizOption(
    val id: String, // "A", "B", "C", "D"
    val text: QuizLocalizedText
)

/**
 * Represents a single question in the question bank.
 */
data class QuizQuestion(
    val id: String,
    val nameId: Int,
    val type: QuizQuestionType,
    val question: QuizLocalizedText,
    val options: List<QuizOption>,
    val correctOption: String
)

/**
 * Represents a question prepared for an active quiz session with shuffled options.
 */
data class QuizSessionQuestion(
    val question: QuizQuestion,
    val shuffledOptions: List<QuizOption>,
    val correctOptionId: String
)
