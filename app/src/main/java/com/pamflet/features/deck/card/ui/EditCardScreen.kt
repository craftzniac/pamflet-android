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
import com.pamflet.features.deck.card.ui.components.DeckSelectDialogInputTrigger
import com.pamflet.features.deck.card.ui.components.DeckSelectInput
import com.pamflet.features.deck.card.ui.components.EditAndPreviewCard
import com.pamflet.shared.ui.components.ErrorSection
import com.pamflet.shared.ui.components.FullscreenLoadingSpinner
import com.pamflet.shared.ui.components.LoadingSpinner
import com.pamflet.shared.ui.components.SimpleTopAppBar
import com.pamflet.shared.ui.theme.Gray50
import com.pamflet.shared.viewmodel.DecksUiState


@Composable
fun EditCardScreen(
    editCardViewModel: EditCardViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAddDeckScreen: () -> Unit
) {
    Scaffold(topBar = {
        SimpleTopAppBar(
            title = "Edit Card", onNavigateBack = onNavigateBack
        )
    }) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier
                    .background(color = Gray50)
                    .fillMaxSize()
            ) {
                item {
                    Box(modifier = Modifier.padding(16.dp)) {
                        DeckSelectDialogInputTrigger(
                            openDialog = { editCardViewModel.openDeckSelectDialog() },
                            selectedDeck = editCardViewModel.cardFormInputUiState.selectedDeck
                        )
                        if (editCardViewModel.isDeckSelectOpenUiState) {
                            DeckSelectInput(
                                onDismissRequest = { editCardViewModel.closeDeckSelectDialog() },
                                decksUiState = editCardViewModel.decksUiState,
                                retryFetchDecks = { editCardViewModel.retryFetchDecks() },
                                updateSelectedDeck = { deck ->
                                    editCardViewModel.updateSelectedDeck(
                                        deck
                                    )
                                },
                                onNavigateToAddDeckScreen = onNavigateToAddDeckScreen,
                                selectedDeck = editCardViewModel.cardFormInputUiState.selectedDeck
                            )
                        }
                    }
                }
                item {
                    Box(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        val cardUiState = editCardViewModel.cardUiState
                        when (cardUiState) {
                            is CardUiState.Loading -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    LoadingSpinner()
                                }
                            }

                            is CardUiState.Error -> {
                                ErrorSection(
                                    message = cardUiState.message,
                                    onAction = { editCardViewModel.retryFetchCard() })
                            }

                            is CardUiState.Success -> {
                                val card = cardUiState.card
                                if (card == null) {
                                    ErrorSection(
                                        message = "Card does not exist",
                                        onAction = onNavigateBack,
                                        actionLabel = "Go back"
                                    )
                                } else {
                                    EditAndPreviewCard(
                                        cardFrontContent = editCardViewModel.cardFormInputUiState.front,
                                        cardBackContent = editCardViewModel.cardFormInputUiState.back,
                                        updateCardBack = { back ->
                                            editCardViewModel.setCardBack(
                                                back
                                            )
                                        },
                                        updateCardFront = { front ->
                                            editCardViewModel.setCardFront(
                                                front
                                            )
                                        },
                                    )
                                }
                            }
                        }
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
                                editCardViewModel.updateCard()
                            },
                            isSubmitting = editCardViewModel.updateCardActionStatusUiState is UpdateCardActionStatus.Submitting,
                            enabled = editCardViewModel.decksUiState is DecksUiState.Success && editCardViewModel.cardUiState is CardUiState.Success && (editCardViewModel.cardUiState as CardUiState.Success).card != null
                        )
                    }
                }
            }
        }
    }
}
