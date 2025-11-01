package com.pamflet.features.deck.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pamflet.core.data.repository.CreateDeckResponse
import com.pamflet.core.data.repository.DeckRepository
import com.pamflet.shared.viewmodel.SharedDecksViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class CreateDeckActionStatus {
    data class Success(val message: String) : CreateDeckActionStatus()
    data class Error(val message: String) : CreateDeckActionStatus()
    data object NotStarted : CreateDeckActionStatus()
    data object Submitting : CreateDeckActionStatus()
}

class AddDeckViewModel(
    val deckRepository: DeckRepository,
    val sharedDecksViewModel: SharedDecksViewModel
) : ViewModel() {
    var deckName by mutableStateOf("")
        private set

    val updateDeckName = { update: String ->
        deckName = update
    }

    var addDeckActionStatus by mutableStateOf<CreateDeckActionStatus>(CreateDeckActionStatus.NotStarted)
        private set

    private fun refetchDecks() {
        sharedDecksViewModel.fetchDecks()
    }

    fun createDeck() {
        viewModelScope.launch {
            addDeckActionStatus = CreateDeckActionStatus.Submitting

            val createDeckResponse = withContext(Dispatchers.IO) {
                deckRepository.createDeck(deckName = deckName)
            }

            when (createDeckResponse) {
                is CreateDeckResponse.Success -> {
                    deckName = ""
                    refetchDecks()
                    addDeckActionStatus =
                        CreateDeckActionStatus.Success("Deck created successfully!")
                }

                is CreateDeckResponse.Error -> {
                    addDeckActionStatus = CreateDeckActionStatus.Error("Deck creation failed!")
                }
            }
        }
    }
}


@Suppress("UNCHECKED_CAST")
class AddDeckViewModelFactory(
    val deckRepository: DeckRepository,
    val sharedDecksViewModel: SharedDecksViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddDeckViewModel(
            deckRepository,
            sharedDecksViewModel
        ) as T
    }
}