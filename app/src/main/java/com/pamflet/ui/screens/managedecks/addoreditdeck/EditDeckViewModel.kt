package com.pamflet.ui.screens.managedecks.addoreditdeck

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.pamflet.NavDestination
import com.pamflet.data.repository.DeckRepository
import com.pamflet.ui.screens.Deck
import com.pamflet.ui.screens.DecksSharedViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditDeckViewModelFactory(
    val editDeckData: NavDestination.EditDeck,
    val decksSharedViewModel: DecksSharedViewModel
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditDeckViewModel(editDeckData, decksSharedViewModel) as T
    }
}

class EditDeckViewModel(
    val editDeckData: NavDestination.EditDeck,
    val decksSharedViewModel: DecksSharedViewModel
) : ViewModel() {
    var deckName by mutableStateOf(editDeckData.deckName)
        private set

    val updateDeckActionStatusMutState = decksSharedViewModel.updateDeckActionStatusMutState

    val updateDeckName = { update: String ->
        deckName = update
    }

    fun updateDeck() {
        val deck = Deck(id = editDeckData.deckId, name = deckName, cardCount = 0)
        decksSharedViewModel.updateDeck(deck)
    }
}
