package com.pamflet.features.deck.card.ui

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pamflet.core.data.local.entity.FlashcardEntity
import com.pamflet.core.domain.Deck
import com.pamflet.core.domain.Flashcard
import com.pamflet.shared.viewmodel.DecksUiState
import com.pamflet.shared.viewmodel.SharedDecksViewModel
import com.pamflet.shared.viewmodel.SharedUiEventViewModel

sealed class AddOrEditCardFormValidation {
    data object Success : AddOrEditCardFormValidation()
    data object NotStarted : AddOrEditCardFormValidation()
    data object EmptyDeckId : AddOrEditCardFormValidation() {
        val message = "You must choose a deck for this card"
    }
}

data class CardFormInputUiState(
    var selectedDeck: Deck?,
    var back: String,
    var front: String,
) {
    fun toFlashcard(cardId: String): Flashcard {
        return Flashcard(
            id = cardId,
            deckId = this.selectedDeck?.id
                ?: throw Exception("Expected selectedDeck to be non-null but it's null"),
            front = this.front,
            back = this.back
        )
    }

    fun toFlashcardEntity(): FlashcardEntity {
        return FlashcardEntity(
            back = this.back,
            front = this.front,
            deckId = this.selectedDeck?.id
                ?: throw Exception("Expected selectedDeck to be non-null but it's null"),
        )
    }

    companion object {
        fun new(selectedDeck: Deck?): CardFormInputUiState {
            return CardFormInputUiState(selectedDeck, "", "")
        }

        fun new(): CardFormInputUiState {
            return CardFormInputUiState(null, "", "")
        }
    }
}

abstract class AddOrEditCardFormStateHolderViewModel(
    private val sharedUiEventViewModel: SharedUiEventViewModel,
    initialSelectedDeckId: String?,
    private val sharedDecksViewModel: SharedDecksViewModel,
) : ViewModel() {
    val decksUiState by sharedDecksViewModel.decksUiStateMutState

    // form input states
    private var _cardFormInputUiMutState = mutableStateOf(
        CardFormInputUiState.new(
            selectedDeck = when (decksUiState) {
                is DecksUiState.Success -> {
                    (decksUiState as DecksUiState.Success).decks.find { it.id == initialSelectedDeckId }
                }

                else -> null
            }
        )
    )

    val cardFormInputUiState: CardFormInputUiState by (_cardFormInputUiMutState as State<CardFormInputUiState>)

    var isDeckSelectOpenUiState by mutableStateOf(false)
        private set

    var formValidationUiState by mutableStateOf<AddOrEditCardFormValidation>(
        AddOrEditCardFormValidation.NotStarted
    )
        private set

    // start -- update form inputs
    val setCardFront = { front: String ->
        _cardFormInputUiMutState.value = _cardFormInputUiMutState.value.copy(front = front)
    }

    val setCardBack = { back: String ->
        _cardFormInputUiMutState.value = _cardFormInputUiMutState.value.copy(back = back)
    }

    fun updateSelectedDeck(deck: Deck) {
        _cardFormInputUiMutState.value = _cardFormInputUiMutState.value.copy(selectedDeck = deck)
    }
    // end -- update form inputs

    fun openDeckSelectDialog() {
        isDeckSelectOpenUiState = true
    }

    fun closeDeckSelectDialog() {
        isDeckSelectOpenUiState = false
    }

    fun emitSnackBarMessage(message: String) {
        sharedUiEventViewModel.emitSnackBarMessage(message)
    }

    fun retryFetchDecks() = sharedDecksViewModel.fetchDecks();
    fun refetchDecks() = sharedDecksViewModel.refetchDecks();

    fun validate(): AddOrEditCardFormValidation {
        val card = this.cardFormInputUiState
        if (card.selectedDeck?.id?.isEmpty() == true) {
            this.formValidationUiState = AddOrEditCardFormValidation.EmptyDeckId
        } else {
            this.formValidationUiState = AddOrEditCardFormValidation.Success
        }
        return this.formValidationUiState
    }
}
