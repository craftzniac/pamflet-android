package com.pamflet.ui.screens.managedecks.addoreditdeck

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pamflet.data.repository.DeckRepository
import com.pamflet.ui.screens.Deck

sealed class DeckUiState {
    data object Loading : DeckUiState()
    data class Error(val message: String) : DeckUiState()
    data class Success(val deck: Deck) : DeckUiState()
}

class EditDeckViewModel(
    val deckId: String,
    val deckRepository: DeckRepository
) : ViewModel() {
    var deckUiState by mutableStateOf<DeckUiState>(DeckUiState.Loading)
        private set

    var deckName by mutableStateOf("")
        private set

    val updateDeckName = { update: String ->
        deckName = update
    }
}
