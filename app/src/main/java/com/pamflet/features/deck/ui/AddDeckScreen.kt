package com.pamflet.features.deck.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pamflet.features.deck.shared.DeckForm
import com.pamflet.shared.ui.components.SimpleTopAppBar
import com.pamflet.shared.ui.theme.Gray50

@Composable
fun AddDeckScreen(
    onNavigateBack: () -> Unit,
    addDeckViewModel: AddDeckViewModel
) {
    val deckName = addDeckViewModel.deckName
    Scaffold(
        topBar = { SimpleTopAppBar(title = "Add Deck", onNavigateBack = onNavigateBack) }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Gray50)
                .padding(contentPadding)
        ) {
            item {
                DeckForm(
                    modifier = Modifier.padding(16.dp),
                    deckName = addDeckViewModel.deckName,
                    updateDeckName = addDeckViewModel.updateDeckName,
                    onSubmit = { addDeckViewModel.createDeck() },
                    isSubmitting = addDeckViewModel.addDeckActionStatus is CreateDeckActionStatus.Submitting,
                    isEnableSubmitBtn = deckName.isNotEmpty() || addDeckViewModel.addDeckActionStatus is CreateDeckActionStatus.Submitting,
                    submitBtnLabel = "Continue"
                )
            }
        }
    }
}
