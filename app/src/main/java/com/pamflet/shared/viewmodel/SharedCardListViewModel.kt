package com.pamflet.shared.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pamflet.core.data.repository.FlashcardRepository
import com.pamflet.core.data.repository.GetAllFlashcardsFromDeckResponse
import com.pamflet.core.domain.Flashcard
import com.pamflet.features.deck.card.ui.FlashcardsUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SharedCardListViewModelFactory(
    private val flashcardRepository: FlashcardRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SharedCardListViewModel(flashcardRepository) as T
    }
}

class SharedCardListViewModel(
    private val flashcardRepository: FlashcardRepository
) : ViewModel() {
    private val _flashcardsUiStateMutState =
        mutableStateOf<FlashcardsUiState>(FlashcardsUiState.Loading)
    val flashcardsUiStateMutState = _flashcardsUiStateMutState as State<FlashcardsUiState>

    private var lastDeckId: String = ""

    // a refetch is used to fetch cards for the current deckId.
    // For a refetch, you want to cache the current card list and show that to the user while the updated card list is being fetched. This means you don't want to show a Loading state
    fun refetchCards() {
        _fetch(lastDeckId)
    }

    // fetches cards for a new deckId. In this case, you want to invalidate the old card list and show a loading state while the card list is being fetched
    fun fetchCards(deckId: String) {
        lastDeckId = deckId
        _flashcardsUiStateMutState.value = FlashcardsUiState.Loading
        _fetch(deckId)
    }

    private fun _fetch(deckId: String) {
        viewModelScope.launch {
            val response =
                withContext(Dispatchers.IO) { flashcardRepository.getAllFromDeck(deckId) }
            _flashcardsUiStateMutState.value = when (response) {
                is GetAllFlashcardsFromDeckResponse.Error -> {
                    FlashcardsUiState.Error(response.message)
                }

                is GetAllFlashcardsFromDeckResponse.Success -> {
                    FlashcardsUiState.Success(
                        response.flashcards.map {
                            Flashcard.fromFlashcardEntity(it)
                        }
                    )
                }
            }
        }
    }
}