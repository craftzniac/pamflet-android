package com.pamflet.features.deck.card.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pamflet.navigation.NavDestination
import com.pamflet.core.data.repository.FlashcardRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.pamflet.core.data.repository.DeckRepository
import com.pamflet.core.data.repository.GetAllFlashcardsFromDeckResponse
import com.pamflet.core.data.repository.GetDeckResponse
import com.pamflet.core.domain.Deck
import com.pamflet.core.domain.Flashcard
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeckCardsListViewModelFactory(
    val deckCardsListNavData: NavDestination.DeckCardsList,
    val flashcardRepository: FlashcardRepository,
    val deckRepository: DeckRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DeckCardsListViewModel(
            deckCardsListNavData, flashcardRepository, deckRepository
        ) as T
    }
}

sealed class FlashcardsUiState {
    data object Loading : FlashcardsUiState()
    data class Success(val cards: List<Flashcard>) : FlashcardsUiState()
    data class Error(val message: String) : FlashcardsUiState()
}

sealed class DeckUiState {
    data object Loading : DeckUiState()
    data class Success(val deck: Deck?) : DeckUiState()
    data class Error(val message: String) : DeckUiState()
    data object NotFound : DeckUiState()
}


class DeckCardsListViewModel(
    val deckCardsListNavData: NavDestination.DeckCardsList,
    val flashcardRepository: FlashcardRepository,
    val deckRepository: DeckRepository
) : ViewModel() {
    var flashcardsUiState by mutableStateOf<FlashcardsUiState>(FlashcardsUiState.Loading)
        private set

    var deckUiState by mutableStateOf<DeckUiState>(DeckUiState.Loading)
        private set

    val deckId = deckCardsListNavData.selectedDeckId

    init {
        fetchDeck()
        fetchFlashcards()
    }

    fun fetchDeck() {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                delay(3000)
                deckRepository.getDeck(deckId)
            }
            deckUiState = when (response) {
                is GetDeckResponse.Error -> {
                    Log.d("fetchDeck", "error occurred: $response")
                    DeckUiState.Error(response.message)
                }

                is GetDeckResponse.Success -> {
                    if (response.deck == null) {
                        DeckUiState.NotFound
                    } else {
                        DeckUiState.Success(Deck.fromDeckEntity(response.deck))
                    }
                }
            }
        }
    }

    fun fetchFlashcards() {
        viewModelScope.launch {
            val response =
                withContext(Dispatchers.IO) { flashcardRepository.getAllFromDeck(deckId) }
            flashcardsUiState = when (response) {
                is GetAllFlashcardsFromDeckResponse.Error -> {
                    FlashcardsUiState.Error(response.message)
                }

                is GetAllFlashcardsFromDeckResponse.Success -> {
                    FlashcardsUiState.Success(
                        response.flashcards.map {
                            Flashcard.fromFlashcardEntity(it)
                        }
                    )
                }
            }
        }
    }
}