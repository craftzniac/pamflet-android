package com.pamflet.features.deck.card.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pamflet.core.data.repository.FlashcardRepository
import com.pamflet.core.domain.Flashcard
import java.util.UUID
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.pamflet.core.data.repository.CreateFlashcardResponse
import com.pamflet.core.domain.Deck
import com.pamflet.shared.viewmodel.SharedDecksViewModel
import com.pamflet.shared.viewmodel.SharedUiEventViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AddCardViewModelFactory(
    val sharedDecksViewModel: SharedDecksViewModel,
    val flashcardRepository: FlashcardRepository,
    val sharedUiEventViewModel: SharedUiEventViewModel,
    val refetchCards: () -> Unit
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST") return AddCardViewModel(
            sharedDecksViewModel,
            flashcardRepository,
            sharedUiEventViewModel,
            refetchCards
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
    private val sharedDecksViewModel: SharedDecksViewModel,
    private val flashcardRepository: FlashcardRepository,
    private val sharedUiEventViewModel: SharedUiEventViewModel,
    private val refetchCards: () -> Unit
) : AddOrEditCardFormStateHolderViewModel() {
    var selectedDeckUiState by mutableStateOf<Deck?>(null)
        private set

    var createCardActionStatusUiState by mutableStateOf<CreateCardActionStatus>(
        CreateCardActionStatus.NotStarted
    )

    val decksUiState by sharedDecksViewModel.decksUiStateMutState
    fun retryFetchDecks() = sharedDecksViewModel.fetchDecks();

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
        this.cardUiState.deckId = deck.id
    }

    fun showSnackBar(message: String) {
        sharedUiEventViewModel.showMessage(message)
    }

    fun createCard() {
        // validate form
        val validationResult = this.validate()
        when (validationResult) {
            is AddOrEditCardFormValidation.NotStarted -> { /* do nothing */
            }

            is AddOrEditCardFormValidation.EmptyDeckId -> {
                showSnackBar(validationResult.message)
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
                            CreateCardActionStatus.Success("Flashcard successfully created!")
                        }
                    }
                }
            }
        }
    }

}