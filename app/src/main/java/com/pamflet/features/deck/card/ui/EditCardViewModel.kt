package com.pamflet.features.deck.card.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pamflet.core.data.repository.FlashcardRepository
import com.pamflet.navigation.NavDestination
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import com.pamflet.core.data.repository.GetFlashcardResponse
import com.pamflet.core.data.repository.UpdateFlashcardResponse
import com.pamflet.core.domain.Flashcard
import com.pamflet.shared.viewmodel.SharedDecksViewModel
import com.pamflet.shared.viewmodel.SharedUiEventViewModel
import kotlinx.coroutines.launch

class EditCardViewModelFactory(
    val editCardNavData: NavDestination.EditCard,
    val flashcardRepository: FlashcardRepository,
    val sharedDecksViewModel: SharedDecksViewModel,
    val sharedUiEventViewModel: SharedUiEventViewModel,
    val refetchCards: () -> Unit,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return EditCardViewModel(
            editCardNavData,
            sharedDecksViewModel = sharedDecksViewModel,
            flashcardRepository = flashcardRepository,
            sharedUiEventViewModel = sharedUiEventViewModel,
            refetchCards = refetchCards
        ) as T
    }
}

sealed class UpdateCardActionStatus {
    data class Error(val message: String) : UpdateCardActionStatus()
    data class Success(val message: String) : UpdateCardActionStatus()
    data object NotStarted : UpdateCardActionStatus()
    data object Submitting : UpdateCardActionStatus()
}

sealed class CardUiState {
    data class Error(val message: String) : CardUiState()
    data class Success(val card: Flashcard?) : CardUiState()
    data object Loading : CardUiState()
}

class EditCardViewModel(
    private val editCardNavData: NavDestination.EditCard,
    val flashcardRepository: FlashcardRepository,
    val sharedDecksViewModel: SharedDecksViewModel,
    val sharedUiEventViewModel: SharedUiEventViewModel,
    private val refetchCards: () -> Unit
) : AddOrEditCardFormStateHolderViewModel(
    sharedUiEventViewModel,
    initialSelectedDeckId = editCardNavData.deckId,
    sharedDecksViewModel
) {
    var updateCardActionStatusUiState by mutableStateOf<UpdateCardActionStatus>(
        UpdateCardActionStatus.NotStarted
    )

    var cardUiState by mutableStateOf<CardUiState>(CardUiState.Loading)

    init {
        fetchCard()
        viewModelScope.launch {
            snapshotFlow { cardUiState }
                .collect { newCardUiState ->
                    when (newCardUiState) {
                        is CardUiState.Success -> {
                            val card = newCardUiState.card
                            if (card != null) {
                                setCardFront(card.front)
                                setCardBack(card.back)
                            }
                        }

                        else -> {/* do nothing */
                        }
                    }
                }
        }
    }

    private fun fetchCard() {
        cardUiState = CardUiState.Loading
        _fetchCard(editCardNavData.selectedCardId, editCardNavData.deckId)
    }

    fun retryFetchCard() = fetchCard()

    private fun _fetchCard(cardId: String, deckId: String) {
        viewModelScope.launch {
            val response = flashcardRepository.get(cardId, deckId)
            cardUiState = when (response) {
                is GetFlashcardResponse.Error -> {
                    CardUiState.Error(response.message)
                }

                is GetFlashcardResponse.Success -> {
                    if (response.flashcard == null) {
                        CardUiState.Success(null)
                    } else {
                        CardUiState.Success(Flashcard.fromFlashcardEntity(response.flashcard))
                    }
                }
            }
        }
    }

    fun updateCard() {
        // validate form
        val validationResult = this.validate()
        when (validationResult) {
            is AddOrEditCardFormValidation.NotStarted -> { /* do nothing */
            }

            is AddOrEditCardFormValidation.EmptyDeckId -> {
                emitSnackBarMessage(validationResult.message)
            }

            is AddOrEditCardFormValidation.Success -> {
                updateCardActionStatusUiState = UpdateCardActionStatus.Submitting
                val flashcardEntity = this.cardFormInputUiState.toFlashcard(
                    cardId = editCardNavData.selectedCardId,
                ).toFlashcardEntity()
                viewModelScope.launch {
                    val response = flashcardRepository.update(flashcardEntity)
                    updateCardActionStatusUiState = when (response) {
                        is UpdateFlashcardResponse.Error -> {
                            emitSnackBarMessage(response.message)
                            UpdateCardActionStatus.Error(response.message)
                        }

                        is UpdateFlashcardResponse.Success -> {
                            refetchCards()
                            refetchDecks()
                            val msg = "Flashcard successfully updated!"
                            emitSnackBarMessage(msg)
                            UpdateCardActionStatus.Success(msg)
                        }
                    }
                }
            }
        }
    }
}