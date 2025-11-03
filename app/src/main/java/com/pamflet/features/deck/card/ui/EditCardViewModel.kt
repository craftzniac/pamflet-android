package com.pamflet.features.deck.card.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pamflet.core.data.repository.FlashcardRepository
import com.pamflet.core.domain.Flashcard
import com.pamflet.navigation.NavDestination
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.pamflet.shared.viewmodel.SharedDecksViewModel

class EditCardViewModelFactory(
    val editCardNavData: NavDestination.EditCard,
    val flashcardRepository: FlashcardRepository,
    val sharedDecksViewModel: SharedDecksViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return EditCardViewModel(editCardNavData, flashcardRepository, sharedDecksViewModel) as T
    }
}

class EditCardViewModel(
    val editCardNavData: NavDestination.EditCard,
    val flashcardRepository: FlashcardRepository,
    val sharedDecksViewModel: SharedDecksViewModel
) : AddOrEditCardFormStateHolderViewModel() {


    val decksUiState by sharedDecksViewModel.decksUiStateMutState
    fun retryFetchDecks() = sharedDecksViewModel.fetchDecks();
}