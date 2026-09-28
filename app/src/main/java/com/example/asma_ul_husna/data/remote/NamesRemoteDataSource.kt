package com.example.asma_ul_husna.data.remote

/**
 * Remote data source contract for fetching Asma-ul-Husna remote data such as audio URLs.
 */
interface NamesRemoteDataSource {
    /**
     * Fetches the mapping of Asma-ul-Husna name number (1..99) to remote audio_url.
     */
    suspend fun fetchAudioUrls(): Result<Map<Int, String>>
}
