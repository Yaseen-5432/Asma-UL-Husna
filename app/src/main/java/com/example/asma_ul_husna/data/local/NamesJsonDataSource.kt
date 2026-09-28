package com.example.asma_ul_husna.data.local

import android.content.Context
import com.example.asma_ul_husna.data.model.AsmaName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Local data source that loads and caches the 99 Names of Allah from assets/names.json.
 * Follows the standardized v2.0 JSON schema.
 */
class NamesJsonDataSource(private val context: Context) : NamesLocalDataSource {

    @Volatile
    private var cachedNames: List<AsmaName>? = null

    /**
     * Loads the 99 names from assets or returns the cached list.
     */
    override suspend fun getNames(): List<AsmaName> = withContext(Dispatchers.IO) {
        cachedNames?.let { return@withContext it }

        synchronized(this) {
            cachedNames?.let { return@synchronized it }

            val jsonString = context.assets.open("names.json").use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    reader.readText()
                }
            }

            val jsonArray = JSONArray(jsonString)
            val namesList = ArrayList<AsmaName>(jsonArray.length())

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val name = AsmaName(
                    id = obj.getInt("id"),
                    arabic = obj.getString("arabic"),
                    transliteration = obj.getString("transliteration"),
                    nameUrdu = obj.getString("name_urdu"),
                    englishMeaning = obj.getString("english_meaning"),
                    meaningUrdu = obj.getString("meaning_urdu"),
                    explanation = obj.getString("explanation"),
                    spiritualBenefits = if (obj.isNull("spiritual_benefits")) null else obj.getString("spiritual_benefits"),
                    quranicReference = if (obj.isNull("quranic_reference")) null else obj.getString("quranic_reference"),
                    audioFilename = if (obj.isNull("audio_filename")) null else obj.getString("audio_filename"),
                    category = if (obj.isNull("category")) null else obj.getString("category")
                )
                namesList.add(name)
            }

            val sortedNames = namesList.sortedBy { it.id }
            cachedNames = sortedNames
            sortedNames
        }
    }

    /**
     * Retrieves a single name by its ID (1-99).
     */
    override suspend fun getNameById(id: Int): AsmaName? {
        val allNames = getNames()
        return allNames.find { it.id == id }
    }
}
