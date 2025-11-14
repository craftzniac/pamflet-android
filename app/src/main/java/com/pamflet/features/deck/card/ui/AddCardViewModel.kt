package com.pamflet.features.deck.card.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pamflet.core.data.repository.FlashcardRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.pamflet.core.data.repository.CreateFlashcardResponse
import com.pamflet.navigation.NavDestination
import com.pamflet.shared.viewmodel.SharedDecksViewModel
import com.pamflet.shared.viewmodel.SharedUiEventViewModel
import kotlinx.coroutines.launch

class AddCardViewModelFactory(
    val addCardNavData: NavDestination.AddCard,
    val sharedDecksViewModel: SharedDecksViewModel,
    val flashcardRepository: FlashcardRepository,
    val sharedUiEventViewModel: SharedUiEventViewModel,
    val refetchCards: () -> Unit,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST") return AddCardViewModel(
            initialSelectedDeckId = addCardNavData.deckId,
            sharedDecksViewModel,
            flashcardRepository,
            sharedUiEventViewModel,
            refetchCards,
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
    sharedDecksViewModel: SharedDecksViewModel,
    private val flashcardRepository: FlashcardRepository,
    sharedUiEventViewModel: SharedUiEventViewModel,
    private val refetchCards: () -> Unit,
) : AddOrEditCardFormStateHolderViewModel(
    sharedUiEventViewModel,
    initialSelectedDeckId,
    sharedDecksViewModel
) {
    var createCardActionStatusUiState by mutableStateOf<CreateCardActionStatus>(
        CreateCardActionStatus.NotStarted
    )

    fun createCard() {
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
                val newFlashcardEntity = this.cardFormInputUiState.toFlashcardEntity()
                viewModelScope.launch {
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