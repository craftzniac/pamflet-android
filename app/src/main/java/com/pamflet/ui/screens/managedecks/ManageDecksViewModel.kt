package com.pamflet.ui.screens.managedecks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pamflet.data.repository.DeckRepository
import com.pamflet.data.repository.DeleteAllFromDeckResponse
import com.pamflet.data.repository.FlashcardRepository
import com.pamflet.ui.screens.Deck
import com.pamflet.ui.screens.DecksSharedViewModel

class ManageDecksViewModel(
    val decksSharedViewModel: DecksSharedViewModel,
    val deckRepository: DeckRepository,
) : ViewModel() {
    val decksUiStateMutState = decksSharedViewModel.decksUiStateMutState
    val deleteDeckActionStatusMutState = decksSharedViewModel.deleteDeckActionStatusMutState

    fun deleteDeck(deck: Deck) = decksSharedViewModel.deleteDeck(deck)
    fun fetchDecks() = decksSharedViewModel.fetchDecks()
}


@Suppress("UNCHECKED_CAST")
class ManageDecksViewModelFactory(
    val decksSharedViewModel: DecksSharedViewModel,
    val deckRepository: DeckRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ManageDecksViewModel(
            decksSharedViewModel,
            deckRepository
        ) as T
    }
}