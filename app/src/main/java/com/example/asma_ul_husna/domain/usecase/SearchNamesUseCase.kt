package com.example.asma_ul_husna.domain.usecase

import com.example.asma_ul_husna.data.model.AsmaName
import com.example.asma_ul_husna.domain.repository.NamesRepository
import com.example.asma_ul_husna.util.SearchUtils

/**
 * Use case to filter and search Names of Allah matching a query string.
 */
class SearchNamesUseCase(
    private val repository: NamesRepository
) {
    suspend operator fun invoke(query: String): List<AsmaName> {
        val allNames = repository.getAllNames()
        if (query.isBlank()) return allNames
        return allNames.filter { SearchUtils.matchesQuery(it, query) }
    }
}
