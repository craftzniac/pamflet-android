package com.pamflet.features.deck.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pamflet.navigation.NavDestination
import com.pamflet.core.domain.Deck
import com.pamflet.shared.viewmodel.SharedDecksViewModel

class EditDeckViewModelFactory(
    val editDeckData: NavDestination.EditDeck,
    val sharedDecksViewModel: SharedDecksViewModel
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditDeckViewModel(editDeckData, sharedDecksViewModel) as T
    }
}

class EditDeckViewModel(
    val editDeckData: NavDestination.EditDeck,
    val sharedDecksViewModel: SharedDecksViewModel
) : ViewModel() {
    var deckName by mutableStateOf(editDeckData.deckName)
        private set

    val updateDeckActionStatusMutState = sharedDecksViewModel.updateDeckActionStatusMutState

    val updateDeckName = { update: String ->
        deckName = update
    }

    fun updateDeck() {
        val deck = Deck(id = editDeckData.deckId, name = deckName, cardCount = 0)
        sharedDecksViewModel.updateDeck(deck)
    }
}
