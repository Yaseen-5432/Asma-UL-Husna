package com.example.asma_ul_husna.data.local

import android.content.Context
import com.example.asma_ul_husna.data.model.QuizLocalizedText
import com.example.asma_ul_husna.data.model.QuizOption
import com.example.asma_ul_husna.data.model.QuizQuestion
import com.example.asma_ul_husna.data.model.QuizQuestionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Loads and caches quiz questions from assets/quiz_questions.json.
 * 100% offline, zero network dependencies.
 */
class QuizJsonDataSource(private val context: Context) : QuizLocalDataSource {

    @Volatile
    private var cachedQuestions: List<QuizQuestion>? = null

    /**
     * Loads all questions from assets or returns the cached list.
     */
    override suspend fun getAllQuestions(): List<QuizQuestion> = withContext(Dispatchers.IO) {
        cachedQuestions?.let { return@withContext it }

        synchronized(this) {
            cachedQuestions?.let { return@synchronized it }

            val jsonString = context.assets.open("quiz_questions.json").use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    reader.readText()
                }
            }

            val rootObj = JSONObject(jsonString)
            val jsonArray = rootObj.getJSONArray("questions")
            val questionsList = ArrayList<QuizQuestion>(jsonArray.length())

            for (i in 0 until jsonArray.length()) {
                val qObj = jsonArray.getJSONObject(i)
                val id = qObj.getString("id")
                val nameId = qObj.getInt("nameId")
                val typeStr = qObj.getString("type")
                val type = when (typeStr.lowercase()) {
                    "meaning" -> QuizQuestionType.MEANING
                    "explanation" -> QuizQuestionType.EXPLANATION
                    "understanding" -> QuizQuestionType.UNDERSTANDING
                    else -> QuizQuestionType.MEANING
                }

                val qTextObj = qObj.getJSONObject("question")
                val qEn = qTextObj.getString("en")
                val qUr = qTextObj.getString("ur")

                val optsObj = qObj.getJSONObject("options")
                val optsEnArray = optsObj.getJSONArray("en")
                val optsUrArray = optsObj.getJSONArray("ur")

                val optionsList = ArrayList<QuizOption>(optsEnArray.length())
                for (j in 0 until optsEnArray.length()) {
                    val optEn = optsEnArray.getJSONObject(j)
                    val optUr = optsUrArray.getJSONObject(j)
                    val optId = optEn.getString("id")
                    optionsList.add(
                        QuizOption(
                            id = optId,
                            text = QuizLocalizedText(
                                en = optEn.getString("text"),
                                ur = optUr.getString("text")
                            )
                        )
                    )
                }

                val correctOption = qObj.getString("correctOption")

                questionsList.add(
                    QuizQuestion(
                        id = id,
                        nameId = nameId,
                        type = type,
                        question = QuizLocalizedText(en = qEn, ur = qUr),
                        options = optionsList,
                        correctOption = correctOption
                    )
                )
            }

            cachedQuestions = questionsList
            questionsList
        }
    }

    /**
     * Retrieves questions for a specific Name ID.
     */
    override suspend fun getQuestionsByNameId(nameId: Int): List<QuizQuestion> {
        val all = getAllQuestions()
        return all.filter { it.nameId == nameId }
    }
}
