package com.pamflet.data.repository

import android.util.Log
import com.pamflet.data.local.dao.FlashcardDao

sealed class DeleteAllFromDeckResponse {
    data class Error(val message: String) : DeleteAllFromDeckResponse()
    data object Success : DeleteAllFromDeckResponse()
}

class FlashcardRepository(
    val flashcardDao: FlashcardDao
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

}