package com.pamflet.features.review.ui

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pamflet.core.data.repository.FlashcardRepository
import com.pamflet.core.domain.Flashcard
import com.pamflet.navigation.NavDestination
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import com.pamflet.core.domain.GetAggregatedFlashcardResponse
import com.pamflet.core.domain.GetAggregatedFlashcardsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class ReviewViewModelFactory(
    private val reviewNavData: NavDestination.Review,
    private val getAggregatedFlashcardsUseCase: GetAggregatedFlashcardsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ReviewViewModel(reviewNavData, getAggregatedFlashcardsUseCase) as T
    }
}

sealed class FlashcardsUiState {
    data object Loading : FlashcardsUiState()
    data class Error(val message: String) : FlashcardsUiState()
    data class Success(val flashcards: List<Flashcard>) : FlashcardsUiState()
}

class ReviewViewModel(
    private val reviewNavData: NavDestination.Review,
    private val getAggregatedFlashcardsUseCase: GetAggregatedFlashcardsUseCase
) : ViewModel() {
    private val _flashcardsUiState = mutableStateOf<FlashcardsUiState>(
        FlashcardsUiState.Loading
    )
    val flashcardsUiState: State<FlashcardsUiState> = _flashcardsUiState

    private val _isFlippedMutState = mutableStateOf(false)
    val isFlippedState: State<Boolean> = _isFlippedMutState

    init {
        fetchFlashcardsForReview()
    }

    fun updateIsFlipped(update: Boolean) {
        _isFlippedMutState.value = update
    }

    fun toggleIsFlipped() {
        _isFlippedMutState.value = !_isFlippedMutState.value
    }

    fun retryFetchFlashcards() = fetchFlashcardsForReview()

    private fun fetchFlashcardsForReview() {
        val deckIds = reviewNavData.selectedDeckIds
        val maxNumberOfCards = reviewNavData.maxNumberOfCards
        val isShuffled = reviewNavData.isShuffleCards
        if (deckIds.isNotEmpty()) {
            _flashcardsUiState.value = FlashcardsUiState.Loading
            viewModelScope.launch {
                delay(3000)
                val response = withContext(Dispatchers.IO) {
                    getAggregatedFlashcardsUseCase(
                        deckIds,
                        maxNumberOfCards,
                        isShuffled
                    )
                }

                _flashcardsUiState.value = when (response) {
                    is GetAggregatedFlashcardResponse.Error -> {
                        FlashcardsUiState.Error(response.message)
                    }

                    is GetAggregatedFlashcardResponse.Success -> {
                        FlashcardsUiState.Success(response.flashcards.map {
                            Flashcard.fromFlashcardEntity(
                                it
                            )
                        })
                    }
                }
            }
        }
    }
}