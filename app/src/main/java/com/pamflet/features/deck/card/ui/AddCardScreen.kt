package com.pamflet.features.deck.card.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pamflet.features.deck.card.ui.components.AddOrEditCardSubmitButton
import com.pamflet.features.deck.card.ui.components.DeckSelectDialog
import com.pamflet.features.deck.card.ui.components.DeckSelectDialogTrigger
import com.pamflet.features.deck.card.ui.components.EditAndPreviewCard
import com.pamflet.shared.ui.components.SimpleTopAppBar
import com.pamflet.shared.ui.theme.Gray50
import com.pamflet.shared.viewmodel.DecksUiState

@Composable
fun AddCardScreen(
    addCardViewModel: AddCardViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAddDeckScreen: () -> Unit
) {
    Scaffold(topBar = {
        SimpleTopAppBar(
            title = "Add Card",
            onNavigateBack = onNavigateBack
        )
    }) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(modifier = Modifier.background(color = Gray50)) {
                item {
                    Box(modifier = Modifier.padding(16.dp)) {
                        DeckSelectDialogTrigger(
                            openDialog = { addCardViewModel.openDeckSelectDialog() },
                            selectedDeck = addCardViewModel.selectedDeckUiState
                        )
                        if (addCardViewModel.isDeckSelectOpenUiState) {
                            DeckSelectDialog(
                                onDismissRequest = { addCardViewModel.closeDeckSelectDialog() },
                                decksUiState = addCardViewModel.decksUiState,
                                retryFetchDecks = { addCardViewModel.retryFetchDecks() },
                                updateSelectedDeck = { deck ->
                                    addCardViewModel.updateSelectedDeck(
                                        deck
                                    )
                                },
                                onNavigateToAddDeckScreen = onNavigateToAddDeckScreen,
                                selectedDeck = addCardViewModel.selectedDeckUiState
                            )
                        }
                    }
                }
                item {
                    Box(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        EditAndPreviewCard(
                            card = addCardViewModel.cardUiState,
                            updateCardBack = { back -> addCardViewModel.setCardBack(back) },
                            updateCardFront = { front -> addCardViewModel.setCardFront(front) }
                        )
                    }
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        AddOrEditCardSubmitButton(
                            onClick = {
                                addCardViewModel.createCard()
                            },
                            isSubmitting = addCardViewModel.createCardActionStatusUiState is CreateCardActionStatus.Submitting,
                            enabled = if (addCardViewModel.decksUiState !is DecksUiState.Success) false else {
                                // add more logic
                                true
                            }
                        )
                    }
                }
            }
        }
    }
}