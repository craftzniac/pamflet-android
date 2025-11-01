package com.pamflet.shared.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pamflet.core.data.repository.DeckRepository
import com.pamflet.core.data.repository.DeleteAllFromDeckResponse
import com.pamflet.core.data.repository.DeleteDeckResponse
import com.pamflet.core.data.repository.FlashcardRepository
import com.pamflet.core.data.repository.GetAllDecksResponse
import com.pamflet.core.data.repository.UpdateDeckResponse
import com.pamflet.core.domain.Deck
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
class SharedDecksViewModelFactory(
    val deckRepository: DeckRepository,
    val flashcardRepository: FlashcardRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SharedDecksViewModel(deckRepository, flashcardRepository) as T
    }
}

sealed class DecksUiState {
    object Loading : DecksUiState()
    data class Success(val decks: List<Deck>) : DecksUiState()
    data class Error(val message: String) : DecksUiState()
}

sealed class DeleteDeckActionStatus {
    object Submitting : DeleteDeckActionStatus()
    object NotStarted : DeleteDeckActionStatus()
    object Success : DeleteDeckActionStatus()
    data class Error(val message: String) : DeleteDeckActionStatus()
}

sealed class UpdateDeckActionStatus {
    object Submitting : UpdateDeckActionStatus()
    object NotStarted : UpdateDeckActionStatus()
    data class Success(val message: String) : UpdateDeckActionStatus()
    data class Error(val message: String) : UpdateDeckActionStatus()
}


class SharedDecksViewModel(
    val deckRepository: DeckRepository,
    val flashcardRepository: FlashcardRepository
) : ViewModel() {
    var deleteDeckActionStatusMutState =
        mutableStateOf<DeleteDeckActionStatus>(DeleteDeckActionStatus.NotStarted)
        private set

    var decksUiStateMutState = mutableStateOf<DecksUiState>(
        DecksUiState.Loading
    )
        private set

    var updateDeckActionStatusMutState = mutableStateOf<UpdateDeckActionStatus>(
        UpdateDeckActionStatus.NotStarted
    )
        private set

    init {
        fetchDecks()
    }

    fun fetchDecks() {
        viewModelScope.launch {
            decksUiStateMutState.value = DecksUiState.Loading
            val response = withContext(Dispatchers.IO) {
                delay(2000)
                deckRepository.getDecks()
            }
            decksUiStateMutState.value = when (response) {
                is GetAllDecksResponse.Success -> {
                    DecksUiState.Success(decks = response.decks.map {
                        Deck(
                            id = it.id,
                            name = it.name,
                            cardCount = it.cardCount
                        )
                    })
                }

                is GetAllDecksResponse.Error -> {
                    DecksUiState.Error(response.message)
                }
            }
        }
    }


    fun deleteDeck(deck: Deck) {
        viewModelScope.launch {
            deleteDeckActionStatusMutState.value = DeleteDeckActionStatus.Submitting
            val response = withContext(Dispatchers.IO) {
                delay(2000)
                flashcardRepository.deleteAllFromDeck(deck.id)
            }
            when (response) {
                is DeleteAllFromDeckResponse.Error -> {
                    deleteDeckActionStatusMutState.value =
                        DeleteDeckActionStatus.Error(response.message)
                }

                is DeleteAllFromDeckResponse.Success -> {
                    val response =
                        withContext(Dispatchers.IO) { deckRepository.deleteDeck(deck.toDeckEntity()) }

                    when (response) {
                        is DeleteDeckResponse.Error -> {
                            deleteDeckActionStatusMutState.value =
                                DeleteDeckActionStatus.Error(response.message)
                        }

                        is DeleteDeckResponse.Success -> {
                            fetchDecks()
                            deleteDeckActionStatusMutState.value = DeleteDeckActionStatus.Success
                        }
                    }
                }
            }
        }
    }

    fun updateDeck(deck: Deck) {
        viewModelScope.launch {
            updateDeckActionStatusMutState.value = UpdateDeckActionStatus.Submitting
            val response = withContext(Dispatchers.IO) {
                deckRepository.updateDeck(deck.toDeckEntity())
            }
            when (response) {
                is UpdateDeckResponse.Error -> {
                    updateDeckActionStatusMutState.value =
                        UpdateDeckActionStatus.Error(message = response.message)
                }

                is UpdateDeckResponse.Success -> {
                    fetchDecks()
                    updateDeckActionStatusMutState.value =
                        UpdateDeckActionStatus.Success(message = response.message)
                }
            }
        }
    }

}