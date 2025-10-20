package com.pamflet.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pamflet.EditorCard
import com.pamflet.Flashcard
import com.pamflet.FlashcardCard
import com.pamflet.NavDestination
import com.pamflet.components.FlashcardFlipButton
import com.pamflet.components.TopAppBarTitleDescriptionText
import com.pamflet.components.topAppBarTitleTextStyle
import com.pamflet.decks
import com.pamflet.ui.theme.Gray50
import com.pamflet.ui.theme.Purple100
import com.pamflet.ui.theme.Red500

enum class CardSelectionMode {
    Edit,
    Preview
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckEditTopAppBar(
    deckName: String,
    deckId: String,   // can be used later for edit and delete buttons
    onNavigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Deck",
                    style = topAppBarTitleTextStyle
                )
                TopAppBarTitleDescriptionText(deckName)
            }
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    painter = painterResource(com.pamflet.R.drawable.arrow_left),
                    contentDescription = "arrow back icon"
                )
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(com.pamflet.R.drawable.pencil),
                    contentDescription = "pencil"
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(com.pamflet.R.drawable.trash_can),
                    contentDescription = "trash can",
                    tint = Red500
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckCardsSlideEditScreen(
    deckCardsSlideEdit: NavDestination.DeckCardsSlideEdit,
    onNavigateBack: () -> Unit
) {
    val deck = decks[0]
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
        topBar = { DeckEditTopAppBar(deckName = deck.name, deckId = deck.id, onNavigateBack) }
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
                                painter = painterResource(com.pamflet.R.drawable.trash_can),
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
                                            painter = painterResource(com.pamflet.R.drawable.text_block),
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
                                            painter = painterResource(com.pamflet.R.drawable.picture),
                                            contentDescription = "picture",
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                )
                            }
                        }
                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(com.pamflet.R.drawable.floppy_disk),
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