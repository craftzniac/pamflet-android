package com.pamflet.features.deck.card.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pamflet.navigation.NavDestination
import com.pamflet.R
import com.pamflet.features.deck.card.ui.components.DeckEditTopAppBar
import com.pamflet.shared.ui.components.ErrorSection
import com.pamflet.shared.ui.components.FullscreenLoadingSpinner
import com.pamflet.shared.ui.theme.Gray50
import com.pamflet.shared.ui.theme.Gray600
import com.pamflet.shared.ui.theme.Gray900
import com.pamflet.shared.ui.theme.Red500

@Composable
fun CardListScreen(
    cardListNavData: NavDestination.CardList,
    onNavigateBack: () -> Unit,
    onNavigateToAddCardScreen: (data: NavDestination.AddCard) -> Unit,
    onNavigateToEditCardScreen: (data: NavDestination.EditCard) -> Unit,
    cardListViewModel: CardListViewModel
) {
    val deckUiState = cardListViewModel.deckUiState
    val flashcardsUiState = cardListViewModel.flashcardsUiState

    LaunchedEffect(cardListNavData.selectedDeckId) {
        val deckId = cardListNavData.selectedDeckId
        cardListViewModel.fetchDeck(deckId)
        cardListViewModel.fetchFlashcards(deckId)
    }

    Scaffold(topBar = { DeckEditTopAppBar(deckUiState, onNavigateBack) }) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Gray50)
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {

            when (deckUiState) {
                is DeckUiState.Loading -> {
                    FullscreenLoadingSpinner()
                }

                is DeckUiState.Error -> {
                    ErrorSection(
                        isFullscreen = true,
                        message = "Try again",
                        detail = deckUiState.message,
                        onAction = { cardListViewModel.refetchFlashcards() })
                }

                is DeckUiState.NotFound -> {
                    ErrorSection(
                        isFullscreen = true,
                        message = "404 Deck does not exist",
                        onAction = onNavigateBack,
                        actionLabel = "Go back"
                    )
                }

                is DeckUiState.Success -> {
                    when (flashcardsUiState) {
                        FlashcardsUiState.Loading -> {
                            FullscreenLoadingSpinner()
                        }

                        is FlashcardsUiState.Error -> {
                            ErrorSection(
                                message = flashcardsUiState.message,
                                isFullscreen = true,
                                onAction = { cardListViewModel.refetchFlashcards() })
                        }

                        is FlashcardsUiState.Success -> {
                            val cards = flashcardsUiState.cards
                            if (cards.isEmpty()) {
                                DeckCardsListScreenEmpty({
                                    val navData =
                                        NavDestination.AddCard(deckId = deckUiState.deck?.id)
                                    onNavigateToAddCardScreen(navData)
                                })
                            } else {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(color = Color.Transparent),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    item {
                                        Row(
                                            horizontalArrangement = Arrangement.End,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                                        ) {
                                            CreateFlashcardButton({
                                                val navData =
                                                    NavDestination.AddCard(deckId = deckUiState.deck?.id)
                                                onNavigateToAddCardScreen(navData)
                                            })
                                        }
                                    }

                                    item {
                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier.padding(
                                                start = 16.dp, end = 16.dp, bottom = 16.dp
                                            )
                                        ) {
                                            cards.forEachIndexed { index, card ->
                                                Card(
                                                    shape = RoundedCornerShape(8.dp),
                                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                                    elevation = CardDefaults.cardElevation(
                                                        defaultElevation = 2.dp
                                                    ),
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable {
                                                            val deckCardsSlideEdit =
                                                                NavDestination.EditCard(
                                                                    selectedCardId = card.id,
                                                                    deckId = card.deckId
                                                                )
                                                            onNavigateToEditCardScreen(
                                                                deckCardsSlideEdit
                                                            )
                                                        },
                                                ) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(
                                                                horizontal = 12.dp, vertical = 8.dp
                                                            ),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(
                                                            8.dp
                                                        )
                                                    ) {
                                                        Text(
                                                            text = "Card ${index + 1}",
                                                            fontSize = 16.sp,
                                                            color = Gray900,
                                                            fontWeight = FontWeight.Medium,
                                                            modifier = Modifier.weight(1F),
                                                            overflow = TextOverflow.Ellipsis,
                                                            maxLines = 1
                                                        )
                                                        IconButton(onClick = {}) {
                                                            Icon(
                                                                painter = painterResource(R.drawable.trash_can),
                                                                contentDescription = "trash can",
                                                                tint = Red500
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun CreateFlashcardButton(onClick: () -> Unit) {
    Button(
        onClick = onClick, shape = RoundedCornerShape(8.dp), modifier = Modifier.height(48.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.plus),
            contentDescription = "",
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text("Add Card")
    }
}

@Composable
fun DeckCardsListScreenEmpty(onNavigateToAddCardScreen: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(
            16.dp, alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "There are no cards in this deck",
            fontSize = 20.sp,
            color = Gray600,
            textAlign = TextAlign.Center
        )
        CreateFlashcardButton(onClick = onNavigateToAddCardScreen)
    }
}