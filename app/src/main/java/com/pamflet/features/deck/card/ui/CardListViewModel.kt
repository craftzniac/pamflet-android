package com.pamflet.features.deck.card.ui

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pamflet.navigation.NavDestination
import com.pamflet.core.data.repository.FlashcardRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import com.pamflet.core.data.repository.DeckRepository
import com.pamflet.core.data.repository.DeleteFlashcardResponse
import com.pamflet.core.data.repository.GetDeckResponse
import com.pamflet.core.domain.Deck
import com.pamflet.core.domain.Flashcard
import com.pamflet.shared.viewmodel.SharedCardListViewModel
import com.pamflet.shared.viewmodel.SharedDecksViewModel
import com.pamflet.shared.viewmodel.SharedUiEventViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeckCardListViewModelFactory(
    private val cardListNavData: NavDestination.CardList,
    private val flashcardRepository: FlashcardRepository,
    private val deckRepository: DeckRepository,
    private val sharedCardListViewModel: SharedCardListViewModel,
    private val sharedUiEventViewModel: SharedUiEventViewModel,
    private val sharedDecksViewModel: SharedDecksViewModel
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CardListViewModel(
            cardListNavData,
            flashcardRepository,
            deckRepository,
            sharedCardListViewModel,
            sharedUiEventViewModel,
            sharedDecksViewModel
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

sealed class DeleteFlashcardActionStatus {
    data object Submitting : DeleteFlashcardActionStatus()
    data object Success : DeleteFlashcardActionStatus()
    data class Error(val message: String) : DeleteFlashcardActionStatus()
    data object NotStarted : DeleteFlashcardActionStatus()
}


class CardListViewModel(
    private val cardListNavData: NavDestination.CardList,
    private val flashcardRepository: FlashcardRepository,
    private val deckRepository: DeckRepository,
    private val sharedCardListViewModel: SharedCardListViewModel,
    private val sharedUiEventViewModel: SharedUiEventViewModel,
    private val sharedDecksViewModel: SharedDecksViewModel
) : ViewModel() {
    var deckUiState by mutableStateOf<DeckUiState>(DeckUiState.Loading)
        private set

    var deletingCardId by mutableStateOf<String?>(null)
        private set
    val selectedDeckId = cardListNavData.selectedDeckId

    var deleteFlashcardActionStatus by mutableStateOf<DeleteFlashcardActionStatus>(
        DeleteFlashcardActionStatus.NotStarted
    )
        private set

    val deleteDeckActionStatuses = sharedDecksViewModel.deleteDeckActionStatuses

    init {
        viewModelScope.launch {
            snapshotFlow { deletingCardId }
                .distinctUntilChanged()
                .collect { cardId ->
                    if (cardId != null) {
                        deleteCard(cardId)
                    }
                    deletingCardId = null
                }
        }

        viewModelScope.launch {
            snapshotFlow { cardListNavData.selectedDeckId }
                .distinctUntilChanged()
                .collect { deckId ->
                    fetchDeck(deckId)
                    fetchFlashcards(deckId)
                }
        }

        viewModelScope.launch {
            snapshotFlow { deleteFlashcardActionStatus }
                .distinctUntilChanged()
                .collect { newValue ->
                    if (newValue is DeleteFlashcardActionStatus.Success ||
                        newValue is DeleteFlashcardActionStatus.Error
                    ) {
                        resetDeleteFlashcardActionStatus()
                    }
                }
        }
    }

    fun updateDeletingCardId(cardId: String) {
        deletingCardId = cardId
    }

    private fun resetDeleteFlashcardActionStatus() {
        deleteFlashcardActionStatus = DeleteFlashcardActionStatus.NotStarted
    }

    val flashcardsUiState by sharedCardListViewModel.flashcardsUiStateMutState

    fun triggerDeckDelete() = sharedDecksViewModel.triggerDeleteDeck(cardListNavData.selectedDeckId)
    fun isDeletingDeckSubmitting() =
        sharedDecksViewModel.isDeletingDeckSubmitting(cardListNavData.selectedDeckId)

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

    private suspend fun deleteCard(flashcardId: String) {
        deleteFlashcardActionStatus = DeleteFlashcardActionStatus.Submitting
        val response = flashcardRepository.deleteOne(flashcardId)
        deleteFlashcardActionStatus = when (response) {
            is DeleteFlashcardResponse.Error -> {
                sharedUiEventViewModel.emitSnackBarMessage(response.message)
                DeleteFlashcardActionStatus.Error(response.message)
            }

            DeleteFlashcardResponse.Success -> {
                refetchFlashcards()
                sharedDecksViewModel.refetchDecks()
                DeleteFlashcardActionStatus.Success
            }
        }
    }
}