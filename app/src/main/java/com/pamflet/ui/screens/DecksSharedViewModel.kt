package com.pamflet.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pamflet.data.repository.DeckRepository
import com.pamflet.data.repository.GetAllDecksResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
class DecksSharedViewModelFactory(
    val deckRepository: DeckRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DecksSharedViewModel(deckRepository) as T
    }
}

sealed class DecksUiState {
    object Loading : DecksUiState()
    data class Success(val decks: List<Deck>) : DecksUiState()
    data class Error(val message: String) : DecksUiState()
}


class DecksSharedViewModel(
    val deckRepository: DeckRepository
) : ViewModel() {

    var decksUiStateMutState = mutableStateOf<DecksUiState>(
        DecksUiState.Loading
    )
        private set

    init {
        fetchDecks()
    }

    fun fetchDecks() {
        viewModelScope.launch {
            decksUiStateMutState.value = DecksUiState.Loading
            val response = withContext(Dispatchers.IO) {
                delay(2000)
                deckRepository.getDecks()
            }
            decksUiStateMutState.value = when (response) {
                is GetAllDecksResponse.Success -> {
                    DecksUiState.Success(decks = response.decks.map {
                        Deck(
                            id = it.id,
                            name = it.name,
                            cardCount = it.cardCount
                        )
                    })
                }

                is GetAllDecksResponse.Error -> {
                    DecksUiState.Error(response.message)
                }
            }
        }
    }
}