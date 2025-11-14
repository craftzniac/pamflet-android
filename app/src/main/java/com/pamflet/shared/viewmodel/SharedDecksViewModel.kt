package com.pamflet.shared.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pamflet.core.data.repository.DeckRepository
import com.pamflet.core.data.repository.GetAllDecksResponse
import com.pamflet.core.data.repository.UpdateDeckResponse
import com.pamflet.core.domain.Deck
import com.pamflet.core.domain.DeleteDeckUseCase
import com.pamflet.core.domain.DeleteDeckUseCaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged

@Suppress("UNCHECKED_CAST")
class SharedDecksViewModelFactory(
    val deckRepository: DeckRepository,
    val deleteDeckUseCase: DeleteDeckUseCase,
    val sharedUiEventViewModel: SharedUiEventViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SharedDecksViewModel(deckRepository, deleteDeckUseCase, sharedUiEventViewModel) as T
    }
}

sealed class DecksUiState {
    object Loading : DecksUiState()
    data class Success(val decks: List<Deck>) : DecksUiState()
    data class Error(val message: String) : DecksUiState()
}

sealed class DeleteDeckActionStatus {
    object Submitting : DeleteDeckActionStatus()
    object AwaitingConfirmation : DeleteDeckActionStatus()
    object Success : DeleteDeckActionStatus()
    data class Error(val message: String) : DeleteDeckActionStatus()
}

sealed class UpdateDeckActionStatus {
    object Submitting : UpdateDeckActionStatus()
    object NotStarted : UpdateDeckActionStatus()
    data class Success(val message: String) : UpdateDeckActionStatus()
    data class Error(val message: String) : UpdateDeckActionStatus()
}

typealias DeckId = String

class SharedDecksViewModel(
    private val deckRepository: DeckRepository,
    private val deleteDeckUseCase: DeleteDeckUseCase,
    private val sharedUiEventViewModel: SharedUiEventViewModel
) : ViewModel() {

    private var _deleteDeckActionStatuses = mutableStateMapOf<DeckId, DeleteDeckActionStatus>()
    val deleteDeckActionStatuses: Map<DeckId, DeleteDeckActionStatus> =
        _deleteDeckActionStatuses

    var deleteDeckAwaitingConfirmation by mutableStateOf<String?>(null)
        private set

    var decksUiStateMutState = mutableStateOf<DecksUiState>(
        DecksUiState.Loading
    )
        private set

    var updateDeckActionStatusMutState = mutableStateOf<UpdateDeckActionStatus>(
        UpdateDeckActionStatus.NotStarted
    )
        private set

    private val tag = "SharedDecksViewModel"

    init {
        viewModelScope.launch {
            snapshotFlow { _deleteDeckActionStatuses.toMap() }
                .distinctUntilChanged()
                .collect { newVal ->
                    Log.d(tag, "deleteDeckActionStatuses: ${_deleteDeckActionStatuses}")
                }
        }

        viewModelScope.launch {
            snapshotFlow { updateDeckActionStatusMutState.value }.collect { newVal ->
                when (newVal) {
                    is UpdateDeckActionStatus.Success, is UpdateDeckActionStatus.Error -> {
                        updateDeckActionStatusMutState.value = UpdateDeckActionStatus.NotStarted
                    }

                    else -> {}
                }
            }
        }

        fetchDecks()
    }

    fun triggerDeleteDeck(deckId: String) {
        _deleteDeckActionStatuses[deckId] = DeleteDeckActionStatus.AwaitingConfirmation
        deleteDeckAwaitingConfirmation = deckId
        sharedUiEventViewModel.openDeleteDeckDialog()
    }

    fun refetchDecks() {
        _fetchDecks()
    }

    fun fetchDecks() { // used for fetching decks for the very first time. All subsequent fetches should be done using the refetchDecks() as you wouldn't want to show a loading spinner since there's already decks in the list albeit outdated data
        decksUiStateMutState.value = DecksUiState.Loading
        _fetchDecks()
    }

    private fun _fetchDecks() {
        viewModelScope.launch {
            val response = withContext(Dispatchers.IO) {
                deckRepository.getDecks()
            }
            decksUiStateMutState.value = when (response) {
                is GetAllDecksResponse.Success -> {
                    DecksUiState.Success(decks = response.decks.map {
                        Deck(
                            id = it.id, name = it.name, cardCount = it.cardCount
                        )
                    })
                }

                is GetAllDecksResponse.Error -> {
                    DecksUiState.Error(response.message)
                }
            }
        }
    }

    fun deleteDeck(deckId: String) {
        // use the AwaitingConfirmation state to know what deckIds are being prepared for delete
        if (_deleteDeckActionStatuses[deckId] == DeleteDeckActionStatus.AwaitingConfirmation) {
            viewModelScope.launch {
                _deleteDeckActionStatuses[deckId] = DeleteDeckActionStatus.Submitting
                val response = deleteDeckUseCase(deckId)
                _deleteDeckActionStatuses[deckId] = when (response) {
                    is DeleteDeckUseCaseResult.Error -> {
                        sharedUiEventViewModel.emitSnackBarMessage(response.message)
                        DeleteDeckActionStatus.Error(response.message)
                    }

                    is DeleteDeckUseCaseResult.Success -> {
                        refetchDecks()
                        sharedUiEventViewModel.emitSnackBarMessage(response.message)
                        sharedUiEventViewModel.closeDeleteDialog()
                        DeleteDeckActionStatus.Success
                    }
                }

                delay(2000)
                removeDeleteDeckAwaitingConfirmation(deckId)
            }
        }
    }

    fun removeDeleteDeckAwaitingConfirmation(deckId: String) {
        _deleteDeckActionStatuses.remove(deckId)
    }

    // check if the deck with the id is being deleted
    fun isDeletingDeckSubmitting(deckId: String): Boolean {
        return if (deckId in deleteDeckActionStatuses) {
            deleteDeckActionStatuses[deckId] == DeleteDeckActionStatus.Submitting
        } else {
            false
        }
    }

    fun updateDeck(deck: Deck) {
        viewModelScope.launch {
            updateDeckActionStatusMutState.value = UpdateDeckActionStatus.Submitting
            val response = deckRepository.updateDeck(deck.toDeckEntity())
            when (response) {
                is UpdateDeckResponse.Error -> {
                    sharedUiEventViewModel.emitSnackBarMessage(response.message)
                    updateDeckActionStatusMutState.value =
                        UpdateDeckActionStatus.Error(message = response.message)
                }

                is UpdateDeckResponse.Success -> {
                    refetchDecks()
                    sharedUiEventViewModel.emitSnackBarMessage(response.message)
                    updateDeckActionStatusMutState.value =
                        UpdateDeckActionStatus.Success(message = response.message)
                }
            }
        }
    }

}