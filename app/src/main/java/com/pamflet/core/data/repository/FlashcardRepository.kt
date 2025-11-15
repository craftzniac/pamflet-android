package com.pamflet.core.data.repository

import android.util.Log
import com.pamflet.core.data.local.dao.FlashcardDao
import com.pamflet.core.data.local.entity.FlashcardEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class DeleteAllFromDeckResponse {
    data class Error(val message: String) : DeleteAllFromDeckResponse()
    data object Success : DeleteAllFromDeckResponse()
}

sealed class DeleteAllFlashcardsResponse {
    data class Error(val message: String) : DeleteAllFlashcardsResponse()
    data object Success : DeleteAllFlashcardsResponse()
}

sealed class GetAllFlashcardsFromDeckResponse {
    data class Error(val message: String) : GetAllFlashcardsFromDeckResponse()
    data class Success(val flashcards: List<FlashcardEntity>) : GetAllFlashcardsFromDeckResponse()
}

sealed class GetFlashcardResponse {
    data class Error(val message: String) : GetFlashcardResponse()
    data class Success(val flashcard: FlashcardEntity?) : GetFlashcardResponse()
}


sealed class CreateFlashcardResponse {
    data class Error(val message: String) : CreateFlashcardResponse()
    data object Success : CreateFlashcardResponse()
}

sealed class UpdateFlashcardResponse {
    data class Error(val message: String) : UpdateFlashcardResponse()
    data object Success : UpdateFlashcardResponse()
}

sealed class DeleteFlashcardResponse {
    data class Error(val message: String) : DeleteFlashcardResponse()
    data object Success : DeleteFlashcardResponse()
}

class FlashcardRepository(
    private val flashcardDao: FlashcardDao
) {

    suspend fun deleteAllFromDeck(deckId: String): DeleteAllFromDeckResponse = withContext(
        Dispatchers.IO
    ) {
        try {
            flashcardDao.deleteAllFromDeck(deckId)
            DeleteAllFromDeckResponse.Success
        } catch (ex: Exception) {
            Log.d("FlashcardRepository::deleteAllFromDeck", "exception: ${ex.message}")
            DeleteAllFromDeckResponse.Error(message = "Couldn't delete")
        }
    }

    suspend fun getAllFromDeck(deckId: String): GetAllFlashcardsFromDeckResponse = withContext(
        Dispatchers.IO
    ) {
        try {
            val cards = flashcardDao.getAll(deckId)
            GetAllFlashcardsFromDeckResponse.Success(cards)
        } catch (ex: Exception) {
            GetAllFlashcardsFromDeckResponse.Error(message = "Couldn't get flashcards")
        }
    }

    suspend fun get(cardId: String, deckId: String) = withContext(Dispatchers.IO) {
        try {
            val flashcard = flashcardDao.getOne(cardId, deckId)
            GetFlashcardResponse.Success(flashcard)
        } catch (ex: Exception) {
            GetFlashcardResponse.Error(message = "Couldn't get flashcard")
        }
    }

    suspend fun create(flashcard: FlashcardEntity): CreateFlashcardResponse = withContext(
        Dispatchers.IO
    ) {
        try {
            flashcardDao.insertOne(flashcard)
            CreateFlashcardResponse.Success
        } catch (ex: Exception) {
            CreateFlashcardResponse.Error("Couldn't create flashcard")
        }
    }

    suspend fun update(flashcard: FlashcardEntity): UpdateFlashcardResponse =
        withContext(Dispatchers.IO) {
            try {
                flashcardDao.updateOne(flashcard)
                UpdateFlashcardResponse.Success
            } catch (ex: Exception) {
                UpdateFlashcardResponse.Error("Couldn't update flashcard")
            }
        }

//    suspend fun deleteOneOrMore(flashcardIds: List<String>) = withContext(Dispatchers.IO) {
//        try {
//            flashcardDao.deleteOneOrMore(ids = flashcardIds)
//            DeleteFlashcardResponse.Success
//        } catch (ex: Exception) {
//            DeleteFlashcardResponse.Error("Couldn't delete flashcards")
//        }
//    }

    suspend fun deleteOne(flashcardId: String) = withContext(Dispatchers.IO) {
        try {
            flashcardDao.deleteOneOrMore(ids = listOf(flashcardId))
            DeleteFlashcardResponse.Success
        } catch (ex: Exception) {
            DeleteFlashcardResponse.Error("Couldn't delete flashcard")
        }
    }

    suspend fun deleteAll() = withContext(Dispatchers.IO) {
        try {
            flashcardDao.deleteAll()
            DeleteAllFlashcardsResponse.Success
        } catch (ex: Exception) {
            DeleteAllFlashcardsResponse.Error("Couldn't delete all flashcards")
        }
    }
}