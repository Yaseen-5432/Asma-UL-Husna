package com.example.asma_ul_husna.data.local

import com.example.asma_ul_husna.data.model.AsmaName

/**
 * Interface defining local data operations for Asma-ul-Husna.
 * This abstraction enables swapping between JSON assets, Room/SQLite, or other local stores.
 */
interface NamesLocalDataSource {
    suspend fun getNames(): List<AsmaName>
    suspend fun getNameById(id: Int): AsmaName?
}
