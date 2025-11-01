package com.pamflet.features.deck.card.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pamflet.shared.ui.components.EditorCard
import com.pamflet.shared.ui.components.FlashcardCard
import com.pamflet.navigation.NavDestination
import com.pamflet.R
import com.pamflet.features.review.ui.dummyDecksWithCards
import com.pamflet.shared.ui.components.FlashcardFlipButton
import com.pamflet.shared.ui.theme.Gray50
import com.pamflet.shared.ui.theme.Red500

enum class CardSelectionMode {
    Edit,
    Preview
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCardScreen(
    editCardNavData: NavDestination.EditCard,
    onNavigateBack: () -> Unit
) {
    val deck = dummyDecksWithCards[0]
    val cards = deck.cards
    val isFlippedMutState = remember { mutableStateOf(false) }
    val pagerState = rememberPagerState { deck.cards.size }
    val cardSelectionModeMutState = remember { mutableStateOf(CardSelectionMode.Preview) }
    val selectedCardMutState = remember { mutableStateOf(cards[pagerState.currentPage]) }

    LaunchedEffect(pagerState.currentPage) {
        selectedCardMutState.value = cards[pagerState.currentPage]
        isFlippedMutState.value = false
    }

    Scaffold(
        topBar = {
//            DeckEditTopAppBar(deckUiState, onNavigateBack)
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Gray50),
                verticalArrangement = Arrangement.spacedBy(
                    2.dp,
                    alignment = Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier
                            .widthIn(max = 300.dp)
                            .fillMaxWidth()
                    ) {
                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(R.drawable.trash_can),
                                contentDescription = "trash can",
                                tint = Red500
                            )
                        }
                        Row(
                            modifier = Modifier.weight(1F),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            SingleChoiceSegmentedButtonRow {
                                SegmentedButton(  // edit button
                                    selected = cardSelectionModeMutState.value == CardSelectionMode.Edit,
                                    onClick = {
                                        cardSelectionModeMutState.value = CardSelectionMode.Edit
                                    },
                                    shape = SegmentedButtonDefaults.itemShape(
                                        index = 0,
                                        count = CardSelectionMode.entries.size
                                    ),
                                    icon = {},
                                    label = {
                                        Icon(
                                            painter = painterResource(R.drawable.text_block),
                                            contentDescription = "text block",
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                )
                                SegmentedButton(  // edit button
                                    selected = cardSelectionModeMutState.value == CardSelectionMode.Preview,
                                    onClick = {
                                        cardSelectionModeMutState.value =
                                            CardSelectionMode.Preview
                                    },
                                    shape = SegmentedButtonDefaults.itemShape(
                                        index = 1,
                                        count = CardSelectionMode.entries.size
                                    ),
                                    icon = {},
                                    label = {
                                        Icon(
                                            painter = painterResource(R.drawable.picture),
                                            contentDescription = "picture",
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                )
                            }
                        }
                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(R.drawable.floppy_disk),
                                contentDescription = "floppy disk"
                            )
                        }
                    }
                }
                item {
                    HorizontalPager(
                        state = pagerState,
                        pageSpacing = 8.dp,
                        contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
                    ) { page ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            when (cardSelectionModeMutState.value) {
                                CardSelectionMode.Edit -> {
                                    EditorCard(
                                        cardMutState = selectedCardMutState,
                                        isFlipped = if (page == pagerState.currentPage) {
                                            isFlippedMutState.value
                                        } else false
                                    )

                                }

                                CardSelectionMode.Preview -> {
                                    FlashcardCard(
                                        card = selectedCardMutState.value,
                                        isFlipped = if (page == pagerState.currentPage) {
                                            isFlippedMutState.value
                                        } else false
                                    )
                                }
                            }
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        FlashcardFlipButton(
                            onClick = { isFlippedMutState.value = !isFlippedMutState.value }
                        )
                    }
                }
            }
        }
    }
}