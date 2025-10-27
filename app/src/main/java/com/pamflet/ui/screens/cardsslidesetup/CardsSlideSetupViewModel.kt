package com.pamflet.ui.screens.cardsslidesetup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pamflet.data.repository.DeckRepository
import com.pamflet.ui.screens.DecksSharedViewModel

@Suppress("UNCHECKED_CAST")
class CardsSlideSetupViewModelFactory(
    val decksSharedViewModel: DecksSharedViewModel, val deckRepository: DeckRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CardsSlideSetupViewModel(
            decksSharedViewModel, deckRepository
        ) as T
    }
}

class CardsSlideSetupViewModel(
    val decksSharedViewModel: DecksSharedViewModel,
    val deckRepository: DeckRepository
) : ViewModel() {
    val decksUiStateMutState = decksSharedViewModel.decksUiStateMutState

    init {
        Log.d("CardsSlideSetupVM", "decksUiState: ${decksUiStateMutState.value}")
    }

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

    fun parseToPositiveInteger(value: String): Int? {
        try {
            val newUnit = value.toUInt()
            return newUnit.toInt()
        } catch (err: NumberFormatException) {
            return null
        }
    }
}