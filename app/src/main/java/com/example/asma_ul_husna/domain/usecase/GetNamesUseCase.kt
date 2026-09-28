package com.example.asma_ul_husna.domain.usecase

import com.example.asma_ul_husna.data.model.AsmaName
import com.example.asma_ul_husna.domain.repository.NamesRepository

/**
 * Use case to retrieve all 99 Names of Allah.
 */
class GetNamesUseCase(
    private val repository: NamesRepository
) {
    suspend operator fun invoke(): List<AsmaName> {
        return repository.getAllNames()
    }
}
