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
import com.pamflet.shared.viewmodel.SharedCardListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeckCardListViewModelFactory(
    val flashcardRepository: FlashcardRepository,
    val deckRepository: DeckRepository,
    val sharedCardListViewModel: SharedCardListViewModel
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CardListViewModel(
            flashcardRepository,
            deckRepository,
            sharedCardListViewModel
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


class CardListViewModel(
    val flashcardRepository: FlashcardRepository,
    val deckRepository: DeckRepository,
    val sharedCardListViewModel: SharedCardListViewModel
) : ViewModel() {
    var deckUiState by mutableStateOf<DeckUiState>(DeckUiState.Loading)
        private set

    val flashcardsUiState by sharedCardListViewModel.flashcardsUiStateMutState

    fun fetchFlashcards(deckId: String) = sharedCardListViewModel.fetchCards(deckId)

    fun refetchFlashcards() = sharedCardListViewModel.refetchCards()

    fun fetchDeck(deckId: String) {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
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
}