package com.example.asma_ul_husna

import com.example.asma_ul_husna.data.model.AppLanguage
import com.example.asma_ul_husna.data.model.QuizLocalizedText
import com.example.asma_ul_husna.data.model.QuizOption
import com.example.asma_ul_husna.data.model.QuizQuestion
import com.example.asma_ul_husna.data.model.QuizQuestionType
import com.example.asma_ul_husna.data.model.QuizSessionQuestion
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class QuizDataValidationTest {

    private fun parseQuizQuestions(): List<QuizQuestion> {
        val file = File("src/main/assets/quiz_questions.json")
        assertTrue("quiz_questions.json file must exist in assets", file.exists())

        val jsonContent = file.readText()
        assertFalse("quiz_questions.json must not be empty", jsonContent.isBlank())

        // Extract questions array content
        val questionsRegex = Regex(""""id"\s*:\s*"([^"]+)",\s*"nameId"\s*:\s*(\d+),\s*"type"\s*:\s*"([^"]+)",\s*"question"\s*:\s*\{([^}]+)\},\s*"options"\s*:\s*\{([\s\S]*?)\},\s*"correctOption"\s*:\s*"([A-D])"""")
        val matches = questionsRegex.findAll(jsonContent).toList()

        val questions = mutableListOf<QuizQuestion>()

        for (m in matches) {
            val qId = m.groupValues[1]
            val nameId = m.groupValues[2].toInt()
            val typeStr = m.groupValues[3]
            val qTextSection = m.groupValues[4]
            val optionsSection = m.groupValues[5]
            val correctOption = m.groupValues[6]

            val enQMatch = Regex(""""en"\s*:\s*"([^"]+)"""").find(qTextSection)
            val urQMatch = Regex(""""ur"\s*:\s*"([^"]+)"""").find(qTextSection)

            assertNotNull("Question EN text required for $qId", enQMatch)
            assertNotNull("Question UR text required for $qId", urQMatch)

            val enQ = enQMatch!!.groupValues[1]
            val urQ = urQMatch!!.groupValues[1]

            // Parse EN options
            val enOptsBlock = Regex(""""en"\s*:\s*\[([\s\S]*?)\]""").find(optionsSection)?.groupValues?.get(1) ?: ""
            val urOptsBlock = Regex(""""ur"\s*:\s*\[([\s\S]*?)\]""").find(optionsSection)?.groupValues?.get(1) ?: ""

            val enOptMatches = Regex("""\{\s*"id"\s*:\s*"([A-D])",\s*"text"\s*:\s*"([^"]+)"\s*\}""").findAll(enOptsBlock).toList()
            val urOptMatches = Regex("""\{\s*"id"\s*:\s*"([A-D])",\s*"text"\s*:\s*"([^"]+)"\s*\}""").findAll(urOptsBlock).toList()

            assertEquals("Each question must have 4 EN options for $qId", 4, enOptMatches.size)
            assertEquals("Each question must have 4 UR options for $qId", 4, urOptMatches.size)

            val optionsList = enOptMatches.mapIndexed { idx, enOpt ->
                val urOpt = urOptMatches[idx]
                QuizOption(
                    id = enOpt.groupValues[1],
                    text = QuizLocalizedText(
                        en = enOpt.groupValues[2],
                        ur = urOpt.groupValues[2]
                    )
                )
            }

            val type = when (typeStr.lowercase()) {
                "meaning" -> QuizQuestionType.MEANING
                "explanation" -> QuizQuestionType.EXPLANATION
                "understanding" -> QuizQuestionType.UNDERSTANDING
                else -> QuizQuestionType.MEANING
            }

            questions.add(
                QuizQuestion(
                    id = qId,
                    nameId = nameId,
                    type = type,
                    question = QuizLocalizedText(en = enQ, ur = urQ),
                    options = optionsList,
                    correctOption = correctOption
                )
            )
        }

        return questions
    }

    @Test
    fun `verify quiz json contains exactly 297 questions for 99 names`() {
        val questions = parseQuizQuestions()
        assertEquals("Total questions must be exactly 297", 297, questions.size)

        val questionsByNameId = questions.groupBy { it.nameId }
        assertEquals("All 99 names must be covered", 99, questionsByNameId.size)

        for (nameId in 1..99) {
            val nameQuestions = questionsByNameId[nameId]
            assertNotNull("Name ID $nameId must have questions", nameQuestions)
            assertEquals("Name ID $nameId must have exactly 3 questions", 3, nameQuestions!!.size)

            val types = nameQuestions.map { it.type }.toSet()
            assertTrue("Name ID $nameId must have MEANING question", types.contains(QuizQuestionType.MEANING))
            assertTrue("Name ID $nameId must have EXPLANATION question", types.contains(QuizQuestionType.EXPLANATION))
            assertTrue("Name ID $nameId must have UNDERSTANDING question", types.contains(QuizQuestionType.UNDERSTANDING))
        }

        val seenIds = mutableSetOf<String>()
        for (q in questions) {
            assertFalse("Duplicate question ID: ${q.id}", seenIds.contains(q.id))
            seenIds.add(q.id)

            assertTrue("Question EN must not be blank", q.question.en.isNotBlank())
            assertTrue("Question UR must not be blank", q.question.ur.isNotBlank())
            assertEquals("Must have 4 options", 4, q.options.size)
            assertTrue("Correct option must be A, B, C, or D", q.correctOption in listOf("A", "B", "C", "D"))

            for (opt in q.options) {
                assertTrue("Option text EN must not be blank", opt.text.en.isNotBlank())
                assertTrue("Option text UR must not be blank", opt.text.ur.isNotBlank())
            }
        }
    }

    @Test
    fun `verify session generation picks 10 unique questions and shuffles options preserving correctness`() {
        val questions = parseQuizQuestions()
        val questionCount = 10
        val letters = listOf("A", "B", "C", "D")

        val selected = questions.shuffled().take(questionCount)
        assertEquals(10, selected.size)

        val sessionQuestions = selected.map { original ->
            val shuffled = original.options.shuffled()
            val remapped = shuffled.mapIndexed { idx, opt ->
                QuizOption(id = letters[idx], text = opt.text)
            }
            val correctOriginalId = original.correctOption
            val correctShuffledIdx = shuffled.indexOfFirst { it.id == correctOriginalId }
            val correctLetter = letters[correctShuffledIdx]

            QuizSessionQuestion(
                question = original,
                shuffledOptions = remapped,
                correctOptionId = correctLetter
            )
        }

        assertEquals(10, sessionQuestions.size)
        val uniqueIds = sessionQuestions.map { it.question.id }.toSet()
        assertEquals("Session questions must be unique", 10, uniqueIds.size)

        for (sq in sessionQuestions) {
            assertEquals(4, sq.shuffledOptions.size)
            assertTrue("Correct option ID must be valid letter", sq.correctOptionId in letters)

            // Verify the option assigned to correctOptionId matches the original correct option text
            val correctOptInSession = sq.shuffledOptions.find { it.id == sq.correctOptionId }
            val originalCorrectOpt = sq.question.options.find { it.id == sq.question.correctOption }

            assertNotNull("Session must contain correct option", correctOptInSession)
            assertNotNull("Original question must contain correct option", originalCorrectOpt)
            assertEquals("English text of correct option must match", originalCorrectOpt!!.text.en, correctOptInSession!!.text.en)
            assertEquals("Urdu text of correct option must match", originalCorrectOpt.text.ur, correctOptInSession.text.ur)
        }
    }

    @Test
    fun `verify localized text resolution for English and Urdu`() {
        val text = QuizLocalizedText(
            en = "The Most Gracious",
            ur = "نہایت مہربان"
        )
        assertEquals("The Most Gracious", text.getText(AppLanguage.ENGLISH))
        assertEquals("The Most Gracious", text.getText(AppLanguage.SYSTEM))
        assertEquals("نہایت مہربان", text.getText(AppLanguage.URDU))
    }
}
