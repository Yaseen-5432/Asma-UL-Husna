package com.example.asma_ul_husna.domain.usecase

import com.example.asma_ul_husna.data.model.AsmaName
import com.example.asma_ul_husna.domain.repository.NamesRepository

/**
 * Use case to retrieve a single Name of Allah by its ID (1-99).
 */
class GetNameDetailUseCase(
    private val repository: NamesRepository
) {
    suspend operator fun invoke(id: Int): AsmaName? {
        return repository.getNameById(id)
    }
}
