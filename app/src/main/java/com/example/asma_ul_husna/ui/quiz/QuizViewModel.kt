package com.example.asma_ul_husna.ui.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.asma_ul_husna.data.model.AppLanguage
import com.example.asma_ul_husna.data.model.AsmaName
import com.example.asma_ul_husna.domain.repository.NamesRepository
import com.example.asma_ul_husna.domain.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QuizViewModel(
    private val quizRepository: QuizRepository,
    private val namesRepository: NamesRepository
) : ViewModel() {

    private val _screenState = MutableStateFlow(QuizScreenState.WELCOME)
    private val _questions = MutableStateFlow<List<com.example.asma_ul_husna.data.model.QuizSessionQuestion>>(emptyList())
    private val _currentQuestionIndex = MutableStateFlow(0)
    private val _selectedOptionId = MutableStateFlow<String?>(null)
    private val _userAnswers = MutableStateFlow<List<QuizUserAnswer>>(emptyList())
    private val _score = MutableStateFlow(0)
    private val _namesMap = MutableStateFlow<Map<Int, AsmaName>>(emptyMap())
    private val _isLoading = MutableStateFlow(false)
    private val _showQuitDialog = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val uiState: StateFlow<QuizUiState> = combine(
        combine(
            _screenState,
            _questions,
            _currentQuestionIndex,
            _selectedOptionId,
            _userAnswers,
            ::QuizInternalPart1
        ),
        combine(
            _score,
            _namesMap,
            _isLoading,
            _showQuitDialog,
            _error,
            ::QuizInternalPart2
        ),
        quizRepository.getLanguagePreference()
    ) { part1, part2, language ->
        QuizUiState(
            screenState = part1.screenState,
            questions = part1.questions,
            currentQuestionIndex = part1.currentQuestionIndex,
            selectedOptionId = part1.selectedOptionId,
            userAnswers = part1.userAnswers,
            score = part2.score,
            totalQuestions = if (part1.questions.isNotEmpty()) part1.questions.size else 10,
            language = language,
            namesMap = part2.namesMap,
            isLoading = part2.isLoading,
            showQuitDialog = part2.showQuitDialog,
            error = part2.error
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = QuizUiState()
    )

    init {
        loadNamesData()
    }

    private fun loadNamesData() {
        viewModelScope.launch {
            try {
                val names = namesRepository.getAllNames()
                _namesMap.value = names.associateBy { it.id }
            } catch (e: Exception) {
                // Non-critical, quiz questions have their own text
            }
        }
    }

    fun startQuiz(questionCount: Int = 10) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                if (_namesMap.value.isEmpty()) {
                    val names = namesRepository.getAllNames()
                    _namesMap.value = names.associateBy { it.id }
                }

                val sessionQuestions = quizRepository.generateQuizSession(questionCount)
                if (sessionQuestions.isEmpty()) {
                    _error.value = "No questions available."
                    return@launch
                }

                _questions.value = sessionQuestions
                _currentQuestionIndex.value = 0
                _selectedOptionId.value = null
                _userAnswers.value = emptyList()
                _score.value = 0
                _showQuitDialog.value = false
                _screenState.value = QuizScreenState.IN_PROGRESS
            } catch (e: Exception) {
                _error.value = e.localizedMessage ?: "Failed to start quiz"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectOption(optionId: String) {
        _selectedOptionId.value = optionId
    }

    fun nextQuestion() {
        val selectedOpt = _selectedOptionId.value ?: return
        val currentQuestions = _questions.value
        val currentIndex = _currentQuestionIndex.value
        val currentQ = currentQuestions.getOrNull(currentIndex) ?: return

        val isCorrect = selectedOpt == currentQ.correctOptionId
        val newAnswer = QuizUserAnswer(
            questionIndex = currentIndex,
            sessionQuestion = currentQ,
            selectedOptionId = selectedOpt,
            isCorrect = isCorrect
        )

        val updatedAnswers = _userAnswers.value + newAnswer
        _userAnswers.value = updatedAnswers

        if (isCorrect) {
            _score.value = _score.value + 1
        }

        if (currentIndex < currentQuestions.size - 1) {
            _currentQuestionIndex.value = currentIndex + 1
            _selectedOptionId.value = null
        } else {
            // Quiz finished
            _screenState.value = QuizScreenState.RESULT
        }
    }

    fun showReview() {
        _screenState.value = QuizScreenState.REVIEW
    }

    fun backToResults() {
        _screenState.value = QuizScreenState.RESULT
    }

    fun showQuitDialog(show: Boolean) {
        _showQuitDialog.value = show
    }

    fun quitQuiz() {
        _showQuitDialog.value = false
        _screenState.value = QuizScreenState.WELCOME
        _questions.value = emptyList()
        _currentQuestionIndex.value = 0
        _selectedOptionId.value = null
        _userAnswers.value = emptyList()
        _score.value = 0
    }
}

private data class QuizInternalPart1(
    val screenState: QuizScreenState,
    val questions: List<com.example.asma_ul_husna.data.model.QuizSessionQuestion>,
    val currentQuestionIndex: Int,
    val selectedOptionId: String?,
    val userAnswers: List<QuizUserAnswer>
)

private data class QuizInternalPart2(
    val score: Int,
    val namesMap: Map<Int, AsmaName>,
    val isLoading: Boolean,
    val showQuitDialog: Boolean,
    val error: String?
)

class QuizViewModelFactory(
    private val quizRepository: QuizRepository,
    private val namesRepository: NamesRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(quizRepository, namesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
