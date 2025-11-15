package com.pamflet.core.domain

import com.pamflet.core.data.repository.DeckRepository
import com.pamflet.core.data.repository.DeleteAllDecksResponse
import com.pamflet.core.data.repository.DeleteAllFlashcardsResponse
import com.pamflet.core.data.repository.FlashcardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

sealed class DeleteAllDecksResult {
    data class Error(val message: String) : DeleteAllDecksResult()
    data class Success(val message: String) : DeleteAllDecksResult()
}

class DeleteAllDecksUseCase(
    val deckRepository: DeckRepository,
    val flashcardRepository: FlashcardRepository
) {
    suspend operator fun invoke(): DeleteAllDecksResult = withContext(Dispatchers.IO) {
        val res1Deferred = async { deckRepository.deleteAll() }
        val res2Deferred = async { flashcardRepository.deleteAll() }
        val res1 = res1Deferred.await()
        val res2 = res2Deferred.await()

        if (res1 is DeleteAllDecksResponse.Error || res2 is DeleteAllFlashcardsResponse.Error) {
            DeleteAllDecksResult.Error(message = "Couldn't clear all decks and cards")
        } else {
            DeleteAllDecksResult.Success(message = "Successfully deleted all decks and cards")
        }
    }
}