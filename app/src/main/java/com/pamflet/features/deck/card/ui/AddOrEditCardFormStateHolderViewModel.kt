package com.pamflet.features.deck.card.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pamflet.core.domain.Flashcard

sealed class AddOrEditCardFormValidation {
    data object Success : AddOrEditCardFormValidation()
    data object NotStarted : AddOrEditCardFormValidation()
    data object EmptyDeckId : AddOrEditCardFormValidation() {
        val message = "You must choose a deck for this card"
    }
}

abstract class AddOrEditCardFormStateHolderViewModel : ViewModel() {
    var formValidationUiState by mutableStateOf<AddOrEditCardFormValidation>(
        AddOrEditCardFormValidation.NotStarted
    )
        private set
    var cardUiState by mutableStateOf(
        Flashcard.new()
    )
        private set

    val setCardFront = { front: String ->
        cardUiState = cardUiState.copy(front = front)
    }
    val setCardBack = { back: String ->
        cardUiState = cardUiState.copy(back = back)
    }

    val setCardDeckId = { deckId: String ->
        cardUiState = cardUiState.copy(deckId = deckId)
    }

    fun validate(): AddOrEditCardFormValidation {
        val card = this.cardUiState
        Log.d("validate", "card: ${card}")
        if (card.deckId.isEmpty()) {
            this.formValidationUiState = AddOrEditCardFormValidation.EmptyDeckId
        } else {
            this.formValidationUiState = AddOrEditCardFormValidation.Success
        }
        return this.formValidationUiState
    }
}
