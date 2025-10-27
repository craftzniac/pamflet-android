package com.pamflet.ui.screens.managedecks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pamflet.data.repository.DeckRepository
import com.pamflet.ui.screens.DecksSharedViewModel

class ManageDecksViewModel(
    val decksSharedViewModel: DecksSharedViewModel,
    val deckRepository: DeckRepository
) : ViewModel() {
    val decksUiStateMutState = decksSharedViewModel.decksUiStateMutState
}


@Suppress("UNCHECKED_CAST")
class ManageDecksViewModelFactory(
    val decksSharedViewModel: DecksSharedViewModel,
    val deckRepository: DeckRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ManageDecksViewModel(
            decksSharedViewModel,
            deckRepository
        ) as T
    }
}