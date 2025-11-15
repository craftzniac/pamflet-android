package com.pamflet.core.domain

import com.pamflet.core.data.local.entity.FlashcardEntity
import com.pamflet.core.data.repository.FlashcardRepository
import com.pamflet.core.data.repository.GetAllFlashcardsFromDeckResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

sealed class GetAggregatedFlashcardResponse {
    data class Error(val message: String) : GetAggregatedFlashcardResponse()
    data class Success(val flashcards: List<FlashcardEntity>) : GetAggregatedFlashcardResponse()
}

class GetAggregatedFlashcardsUseCase(
    private val flashcardRepository: FlashcardRepository
) {
    suspend operator fun invoke(
        deckIds: List<String>, maxNumberOfCards: Int, isShuffle: Boolean
    ): GetAggregatedFlashcardResponse {
        val cardsAggregate: MutableList<FlashcardEntity> = mutableListOf()
        val cardsFromEachDeck = withContext(Dispatchers.IO) {
            deckIds.map { it ->
                async { flashcardRepository.getAllFromDeck(it) }
            }.awaitAll()
        }
        for (response in cardsFromEachDeck) {
            when (response) {
                is GetAllFlashcardsFromDeckResponse.Error -> {
                    return GetAggregatedFlashcardResponse.Error(message = "Couldn't get flashcards")
                }

                is GetAllFlashcardsFromDeckResponse.Success -> {
                    cardsAggregate.addAll(response.flashcards)
                }
            }
        }

        if (isShuffle) {
            cardsAggregate.shuffle()
        }
        return GetAggregatedFlashcardResponse.Success(cardsAggregate.take(maxNumberOfCards))
    }
}