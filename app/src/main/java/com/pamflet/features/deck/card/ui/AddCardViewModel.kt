package com.pamflet.features.deck.card.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pamflet.core.data.repository.FlashcardRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.pamflet.core.data.repository.CreateFlashcardResponse
import com.pamflet.core.domain.Deck
import com.pamflet.navigation.NavDestination
import com.pamflet.shared.viewmodel.DecksUiState
import com.pamflet.shared.viewmodel.SharedDecksViewModel
import com.pamflet.shared.viewmodel.SharedUiEventViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddCardViewModelFactory(
    val addCardNavData: NavDestination.AddCard,
    val sharedDecksViewModel: SharedDecksViewModel,
    val flashcardRepository: FlashcardRepository,
    val sharedUiEventViewModel: SharedUiEventViewModel,
    val refetchCards: () -> Unit,
    val fetchCards: (String) -> Unit,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST") return AddCardViewModel(
            initialSelectedDeckId = addCardNavData.deckId,
            sharedDecksViewModel,
            flashcardRepository,
            sharedUiEventViewModel,
            refetchCards,
            fetchCards,
        ) as T
    }
}

sealed class CreateCardActionStatus {
    data class Error(val message: String) : CreateCardActionStatus()
    data class Success(val message: String) : CreateCardActionStatus()
    data object NotStarted : CreateCardActionStatus()
    data object Submitting : CreateCardActionStatus()
}

class AddCardViewModel(
    initialSelectedDeckId: String?,
    private val sharedDecksViewModel: SharedDecksViewModel,
    private val flashcardRepository: FlashcardRepository,
    private val sharedUiEventViewModel: SharedUiEventViewModel,
    private val refetchCards: () -> Unit,
    private val fetchCards: (String) -> Unit,
) : AddOrEditCardFormStateHolderViewModel() {
    val decksUiState by sharedDecksViewModel.decksUiStateMutState

    // state used by the deck select menu
    var selectedDeckUiState by mutableStateOf(
        value = when (decksUiState) {
            is DecksUiState.Success -> {
                (decksUiState as DecksUiState.Success).decks.find { it.id == initialSelectedDeckId }
            }

            else -> null
        }
    )
        private set

    var createCardActionStatusUiState by mutableStateOf<CreateCardActionStatus>(
        CreateCardActionStatus.NotStarted
    )

    fun retryFetchDecks() = sharedDecksViewModel.fetchDecks();
    fun refetchDecks() = sharedDecksViewModel.refetchDecks();

    var isDeckSelectOpenUiState by mutableStateOf(false)
        private set

    fun openDeckSelectDialog() {
        isDeckSelectOpenUiState = true
    }

    fun closeDeckSelectDialog() {
        isDeckSelectOpenUiState = false
    }

    fun updateSelectedDeck(deck: Deck) {
        selectedDeckUiState = deck
    }

    fun emitSnackBarMessage(message: String) {
        sharedUiEventViewModel.emitSnackBarMessage(message)
    }

    fun createCard() {
        this.cardUiState.deckId = selectedDeckUiState?.id ?: ""
        // validate form
        val validationResult = this.validate()
        when (validationResult) {
            is AddOrEditCardFormValidation.NotStarted -> { /* do nothing */
            }

            is AddOrEditCardFormValidation.EmptyDeckId -> {
                emitSnackBarMessage(validationResult.message)
            }

            is AddOrEditCardFormValidation.Success -> {
                createCardActionStatusUiState = CreateCardActionStatus.Submitting
                val newFlashcardEntity = this.cardUiState.toFlashcardEntity()
                viewModelScope.launch {
                    delay(2000)
                    val response = flashcardRepository.create(newFlashcardEntity)
                    createCardActionStatusUiState = when (response) {
                        is CreateFlashcardResponse.Error -> {
                            CreateCardActionStatus.Error(response.message)
                        }

                        is CreateFlashcardResponse.Success -> {
                            refetchCards()
                            refetchDecks()
                            CreateCardActionStatus.Success("Flashcard successfully created!")
                        }
                    }
                }
            }
        }
    }

}