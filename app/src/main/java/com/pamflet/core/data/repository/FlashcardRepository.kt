package com.pamflet.core.data.repository

import android.util.Log
import com.pamflet.core.data.local.dao.FlashcardDao
import com.pamflet.core.data.local.entity.FlashcardEntity

sealed class DeleteAllFromDeckResponse {
    data class Error(val message: String) : DeleteAllFromDeckResponse()
    data object Success : DeleteAllFromDeckResponse()
}

sealed class GetAllFlashcardsFromDeckResponse {
    data class Error(val message: String) : GetAllFlashcardsFromDeckResponse()
    data class Success(val flashcards: List<FlashcardEntity>) : GetAllFlashcardsFromDeckResponse()
}

class FlashcardRepository(
    private val flashcardDao: FlashcardDao
) {

    suspend fun deleteAllFromDeck(deckId: String): DeleteAllFromDeckResponse {
        return try {
            flashcardDao.deleteAllFromDeck(deckId)
            DeleteAllFromDeckResponse.Success
        } catch (ex: Exception) {
            Log.d("FlashcardRepository::deleteAllFromDeck", "exception: ${ex.message}")
            DeleteAllFromDeckResponse.Error(message = "Couldn't delete")
        }
    }

    suspend fun getAllFromDeck(deckId: String): GetAllFlashcardsFromDeckResponse {
        return try {
            val cards = flashcardDao.getAll(deckId)
            GetAllFlashcardsFromDeckResponse.Success(cards)
        } catch (ex: Exception) {
            GetAllFlashcardsFromDeckResponse.Error(message = "Couldn't get flashcards")
        }
    }

}