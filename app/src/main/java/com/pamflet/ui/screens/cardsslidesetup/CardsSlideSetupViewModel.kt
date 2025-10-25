package com.pamflet.ui.screens.cardsslidesetup

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pamflet.data.repository.DeckRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.pamflet.ui.screens.Deck
import kotlinx.coroutines.delay
import com.pamflet.data.repository.deckEntities

@Suppress("UNCHECKED_CAST")
class CardsSlideSetupViewModelFactory(
    val deckRepository: DeckRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CardsSlideSetupViewModel(deckRepository) as T
    }
}

sealed class DecksUiState {
    object Loading : DecksUiState()
    data class Success(val decks: List<Deck>) : DecksUiState()
    data class Error(val error: String) : DecksUiState()
}

class CardsSlideSetupViewModel(
    val deckRepository: DeckRepository
) : ViewModel() {
    var decksUiState by mutableStateOf<DecksUiState>(
        DecksUiState.Loading
    )
        private set

    private val maxNumberOfCardsMin: UInt =
        5u     // the minimum value acceptable for maxNumberOfCards
    private val maxNumberOfCardsMax: UInt =
        1000u  // the maximum value acceptable for maxNumberOfCards
    private val maxNumberOfCardsInitialVal = 100
    private val step = 1

    var maxNumberOfCards by mutableIntStateOf(maxNumberOfCardsInitialVal)
        private set

    var isShuffleCards by mutableStateOf(false)
        private set

    var selectedDeckIds by mutableStateOf(
        listOf<String>()
    )
        private set


    init {
        getDecks()
    }

    fun incrementMaxNumberOfCards() {
        if (maxNumberOfCards + step > maxNumberOfCardsMax.toInt()) {
            maxNumberOfCards = maxNumberOfCardsMax.toInt()
        } else {
            maxNumberOfCards += step
        }
    }

    fun decrementMaxNumberOfCards() {
        if (maxNumberOfCards - step < maxNumberOfCardsMin.toInt()) {
            maxNumberOfCards = maxNumberOfCardsMin.toInt()
        } else {
            maxNumberOfCards -= step
        }
    }

    fun rawUpdateMaxNumberOfCards(rawString: String) {
        val rawInt = parseToPositiveInteger(rawString)
        maxNumberOfCards = if (rawInt == null || rawInt < maxNumberOfCardsMin.toInt()) {
            maxNumberOfCardsMin.toInt()
        } else if (rawInt > maxNumberOfCardsMax.toInt()) {
            maxNumberOfCardsMax.toInt()
        } else {
            rawInt
        }
    }

    val setIsShuffleCards = { update: Boolean ->
        isShuffleCards = update
    }

    fun toggleDeckSelection(deckId: String) {
        selectedDeckIds = if (deckId in selectedDeckIds) {
            // remove it
            selectedDeckIds.filter { it != deckId }
        } else {
            // add it to list
            selectedDeckIds.plus(listOf(deckId))
        }
    }

    fun isDeckSelected(deckId: String): Boolean {
        return deckId in selectedDeckIds
    }

    fun getDecks() {
        viewModelScope.launch {
            when (decksUiState) {
                is DecksUiState.Success -> { /* only switch to the loading state if the data is being fetched for the first time */
                }

                else -> {
                    decksUiState = DecksUiState.Loading
                }
            }
            val decksResponse = withContext(Dispatchers.IO) {
                delay(2000)
                deckRepository.getDecks()
                deckEntities
            }
            decksUiState = DecksUiState.Success(decks = decksResponse.map {
                Deck(
                    id = it.id,
                    name = it.name,
                )
            })
        }
    }

    fun parseToPositiveInteger(value: String): Int? {
        try {
            val newUnit = value.toUInt()
            return newUnit.toInt()
        } catch (err: NumberFormatException) {
            return null
        }
    }
}