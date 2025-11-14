package com.pamflet.core.domain

import com.pamflet.core.data.repository.DeckRepository
import com.pamflet.core.data.repository.DeleteAllFromDeckResponse
import com.pamflet.core.data.repository.DeleteDeckResponse
import com.pamflet.core.data.repository.FlashcardRepository

sealed class DeleteDeckUseCaseResult {
    data class Error(val message: String) : DeleteDeckUseCaseResult()
    data class Success(val message: String) : DeleteDeckUseCaseResult()
}

class DeleteDeckUseCase(
    val deckRepository: DeckRepository,
    val flashcardRepository: FlashcardRepository,
) {
    suspend operator fun invoke(deckId: String): DeleteDeckUseCaseResult {
        // delete cards for deck
        val res1 = flashcardRepository.deleteAllFromDeck(deckId)
        val res2 = deckRepository.deleteDeck(deckId)
        return if (res1 is DeleteAllFromDeckResponse.Success && res2 is DeleteDeckResponse.Success) {
            DeleteDeckUseCaseResult.Success(message = "Deck deleted successfully")
        } else {
            DeleteDeckUseCaseResult.Error(message = "Couldn't complete deleting deck")
        }
    }
}