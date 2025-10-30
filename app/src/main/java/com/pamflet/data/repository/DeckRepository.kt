package com.pamflet.data.repository

import android.util.Log
import com.pamflet.data.local.dao.DeckDao
import com.pamflet.data.local.dao.DeckEntityWithCardCount
import com.pamflet.data.local.entity.DeckEntity

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
    suspend fun getDecks(): GetAllDecksResponse {
        return try {
            val decks = deckDao.getAll()
            GetAllDecksResponse.Success(decks)
        } catch (ex: Exception) {
            GetAllDecksResponse.Error("Something went wrong")
        }
    }

    suspend fun createDeck(deckName: String): CreateDeckResponse {
        return try {
            val newDeckId = deckDao.createOne(DeckEntity(name = deckName))
            CreateDeckResponse.Success(newDeckId.toString())
        } catch (ex: Exception) {
            CreateDeckResponse.Error("Something went wrong")
        }
    }

    suspend fun deleteDeck(deck: DeckEntity): DeleteDeckResponse {
        return try {
            deckDao.deleteOne(deck)
            DeleteDeckResponse.Success
        } catch (err: Exception) {
            DeleteDeckResponse.Error("Couldn't delete deck")
        }
    }

    suspend fun updateDeck(deck: DeckEntity): UpdateDeckResponse {
        return try {
            deckDao.updateOne(deck)
            UpdateDeckResponse.Success("Deck was successfully updated!")
        } catch (err: Exception) {
            UpdateDeckResponse.Error("Deck couldn't be updated")
        }
    }

    suspend fun getDeck(deckId: String): GetDeckResponse {
        return try {
            Log.d("DeckRepository::getDeck", "deckId: $deckId")
            val deckEntity = deckDao.getOne(deckId)
            GetDeckResponse.Success(deckEntity)
        } catch (ex: Exception) {
            Log.d("DeckRepository::getDeck", "error: $ex")
            GetDeckResponse.Error(message = "Something went wrong, couldn't get deck")
        }
    }

}
