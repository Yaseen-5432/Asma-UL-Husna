package com.example.asma_ul_husna.ui.quiz

import com.example.asma_ul_husna.data.model.AppLanguage
import com.example.asma_ul_husna.data.model.AsmaName
import com.example.asma_ul_husna.data.model.QuizSessionQuestion

enum class QuizScreenState {
    WELCOME,
    IN_PROGRESS,
    RESULT,
    REVIEW
}

data class QuizUserAnswer(
    val questionIndex: Int,
    val sessionQuestion: QuizSessionQuestion,
    val selectedOptionId: String,
    val isCorrect: Boolean
)

data class QuizUiState(
    val screenState: QuizScreenState = QuizScreenState.WELCOME,
    val questions: List<QuizSessionQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedOptionId: String? = null,
    val userAnswers: List<QuizUserAnswer> = emptyList(),
    val score: Int = 0,
    val totalQuestions: Int = 10,
    val language: AppLanguage = AppLanguage.SYSTEM,
    val namesMap: Map<Int, AsmaName> = emptyMap(),
    val isLoading: Boolean = false,
    val showQuitDialog: Boolean = false,
    val error: String? = null
) {
    val currentQuestion: QuizSessionQuestion?
        get() = questions.getOrNull(currentQuestionIndex)

    val currentName: AsmaName?
        get() = currentQuestion?.let { namesMap[it.question.nameId] }

    val progress: Float
        get() = if (totalQuestions > 0) (currentQuestionIndex + 1).toFloat() / totalQuestions else 0f

    val percentageScore: Int
        get() = if (totalQuestions > 0) ((score * 100) / totalQuestions) else 0

    val isLastQuestion: Boolean
        get() = currentQuestionIndex >= totalQuestions - 1

    val isOptionSelected: Boolean
        get() = selectedOptionId != null
}
