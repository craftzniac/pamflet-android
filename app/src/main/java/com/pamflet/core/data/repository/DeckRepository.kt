package com.pamflet.core.data.repository

import android.util.Log
import com.pamflet.core.data.local.dao.DeckDao
import com.pamflet.core.data.local.dao.DeckEntityWithCardCount
import com.pamflet.core.data.local.entity.DeckEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class CreateDeckResponse {
    data class Success(val newDeckId: String) : CreateDeckResponse()
    data class Error(val message: String) : CreateDeckResponse()
}

sealed class DeleteDeckResponse {
    data object Success : DeleteDeckResponse()
    data class Error(val message: String) : DeleteDeckResponse()
}

sealed class UpdateDeckResponse {
    data class Success(val message: String) : UpdateDeckResponse()
    data class Error(val message: String) : UpdateDeckResponse()
}

sealed class GetDeckResponse {
    data class Success(val deck: DeckEntity?) : GetDeckResponse()
    data class Error(val message: String) : GetDeckResponse()
}

sealed class GetAllDecksResponse {
    data class Success(val decks: List<DeckEntityWithCardCount>) :
        GetAllDecksResponse()

    data class Error(val message: String) : GetAllDecksResponse()
}

class DeckRepository(
    private val deckDao: DeckDao,
) {
    suspend fun getDecks(): GetAllDecksResponse = withContext(Dispatchers.IO) {
        try {
            val decks = deckDao.getAll()
            GetAllDecksResponse.Success(decks)
        } catch (ex: Exception) {
            GetAllDecksResponse.Error("Something went wrong")
        }
    }

    suspend fun createDeck(deckName: String): CreateDeckResponse = withContext(Dispatchers.IO) {
        try {
            val newDeckId = deckDao.createOne(DeckEntity(name = deckName))
            CreateDeckResponse.Success(newDeckId.toString())
        } catch (ex: Exception) {
            CreateDeckResponse.Error("Something went wrong")
        }
    }

    suspend fun deleteDeck(deckId: String): DeleteDeckResponse = withContext(Dispatchers.IO) {
        try {
            deckDao.deleteOne(deckId)
            DeleteDeckResponse.Success
        } catch (err: Exception) {
            DeleteDeckResponse.Error("Couldn't delete deck")
        }
    }

    suspend fun updateDeck(deck: DeckEntity): UpdateDeckResponse = withContext(Dispatchers.IO) {
        try {
            deckDao.updateOne(deck)
            UpdateDeckResponse.Success("Deck was successfully updated!")
        } catch (err: Exception) {
            UpdateDeckResponse.Error("Deck couldn't be updated")
        }
    }

    suspend fun getDeck(deckId: String): GetDeckResponse = withContext(Dispatchers.IO) {
        try {
            Log.d("DeckRepository::getDeck", "deckId: $deckId")
            val deckEntity = deckDao.getOne(deckId)
            GetDeckResponse.Success(deckEntity)
        } catch (ex: Exception) {
            Log.d("DeckRepository::getDeck", "error: $ex")
            GetDeckResponse.Error(message = "Something went wrong, couldn't get deck")
        }
    }

}
