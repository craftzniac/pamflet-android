package com.pamflet.data.repository

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
            val updatedValuesCount = deckDao.updateOne(deck)
            if (updatedValuesCount == 0) {
                UpdateDeckResponse.Error("Deck does not exist")
            } else {
                UpdateDeckResponse.Success("Deck was successfully updated!")
            }
        } catch (err: Exception) {
            UpdateDeckResponse.Error("Deck couldn't be updated")
        }
    }

}
