package com.example.asma_ul_husna.domain.usecase

import com.example.asma_ul_husna.domain.repository.NamesRepository

/**
 * Use case to toggle favorite status for a given Name ID.
 */
class ToggleFavoriteUseCase(
    private val repository: NamesRepository
) {
    suspend operator fun invoke(id: Int) {
        repository.toggleFavorite(id)
    }
}
