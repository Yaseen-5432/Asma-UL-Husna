package com.example.asma_ul_husna.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Remote API implementation that fetches Asma-ul-Husna audio URLs from the free API.
 * Endpoint: https://asmaul-husna-api-coral.vercel.app/api/asmaul-husna?lang=english
 */
class NamesApiDataSource(
    private val apiUrl: String = API_ENDPOINT
) : NamesRemoteDataSource {

    companion object {
        const val API_ENDPOINT = "https://asmaul-husna-api-coral.vercel.app/api/asmaul-husna?lang=english"
        private const val CONNECT_TIMEOUT_MS = 15000
        private const val READ_TIMEOUT_MS = 15000
    }

    @Volatile
    private var cachedAudioUrls: Map<Int, String>? = null

    override suspend fun fetchAudioUrls(): Result<Map<Int, String>> = withContext(Dispatchers.IO) {
        cachedAudioUrls?.let { return@withContext Result.success(it) }

        var connection: HttpURLConnection? = null
        try {
            val url = URL(apiUrl)
            connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = CONNECT_TIMEOUT_MS
                readTimeout = READ_TIMEOUT_MS
                setRequestProperty("Accept", "application/json")
                setRequestProperty("User-Agent", "AsmaulHusna-Android/1.0")
                instanceFollowRedirects = true
            }

            val responseCode = connection.responseCode
            if (responseCode !in 200..299) {
                return@withContext Result.failure(
                    Exception("HTTP error fetching audio URLs: $responseCode ${connection.responseMessage}")
                )
            }

            val responseText = connection.inputStream.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream, Charsets.UTF_8)).use { reader ->
                    reader.readText()
                }
            }

            val jsonObject = JSONObject(responseText)
            val resultsArray = jsonObject.optJSONArray("results")
                ?: return@withContext Result.failure(Exception("Invalid API response: missing results array"))

            val audioMap = HashMap<Int, String>(resultsArray.length())
            for (i in 0 until resultsArray.length()) {
                val item = resultsArray.getJSONObject(i)
                val number = item.optInt("number", -1)
                val audioUrl = item.optString("audio_url", "")

                if (number in 1..99 && audioUrl.isNotBlank() && audioUrl.startsWith("http")) {
                    audioMap[number] = audioUrl
                }
            }

            if (audioMap.isEmpty()) {
                return@withContext Result.failure(Exception("No audio URLs found in API response"))
            }

            cachedAudioUrls = audioMap
            Result.success(audioMap)
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            connection?.disconnect()
        }
    }
}
