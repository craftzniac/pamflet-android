package com.pamflet.features.deck.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pamflet.core.domain.Deck
import com.pamflet.shared.viewmodel.SharedDecksViewModel

class ManageDecksViewModel(
    val sharedDecksViewModel: SharedDecksViewModel,
) : ViewModel() {
    val decksUiStateMutState = sharedDecksViewModel.decksUiStateMutState
    val deleteDeckActionStatusMutState = sharedDecksViewModel.deleteDeckActionStatusMutState

    fun deleteDeck(deck: Deck) = sharedDecksViewModel.deleteDeck(deck)
    fun fetchDecks() = sharedDecksViewModel.fetchDecks()
}


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