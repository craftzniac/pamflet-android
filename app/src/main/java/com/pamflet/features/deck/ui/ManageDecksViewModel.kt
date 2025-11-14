package com.pamflet.features.deck.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pamflet.shared.viewmodel.DeckId
import com.pamflet.shared.viewmodel.SharedDecksViewModel

@Suppress("UNCHECKED_CAST")
class ManageDecksViewModelFactory(
    val sharedDecksViewModel: SharedDecksViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ManageDecksViewModel(
            sharedDecksViewModel
        ) as T
    }
}

class ManageDecksViewModel(
    private val sharedDecksViewModel: SharedDecksViewModel,
) : ViewModel() {
    val decksUiStateMutState = sharedDecksViewModel.decksUiStateMutState
    fun fetchDecks() = sharedDecksViewModel.fetchDecks()
    fun triggerDeckDelete(deckId: String) = sharedDecksViewModel.triggerDeleteDeck(deckId)
    fun isDeletingDeckSubmitting(deckId: String) = sharedDecksViewModel.isDeletingDeckSubmitting(deckId)
}