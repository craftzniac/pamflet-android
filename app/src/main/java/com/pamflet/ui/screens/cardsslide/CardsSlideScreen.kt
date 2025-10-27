package com.pamflet.ui.screens.cardsslide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import com.pamflet.ui.components.FlashcardCard
import com.pamflet.NavDestination
import com.pamflet.R
import com.pamflet.ui.components.FlashcardFlipButton
import com.pamflet.ui.components.TopAppBarTitleDescriptionText
import com.pamflet.ui.components.topAppBarTitleTextStyle
import com.pamflet.ui.screens.Deck
import com.pamflet.ui.screens.DeckWithCards
import com.pamflet.ui.screens.Flashcard
import com.pamflet.ui.theme.Gray50
import java.util.UUID


val dummyCards = listOf(
    Flashcard(
        id = UUID.randomUUID().toString(),
        front = "something",
        back = "nothing"
    ),
    Flashcard(
        id = UUID.randomUUID().toString(),
        front = "Somebody name",
        back = "Another thing to note"
    ),
)

val dummyDecksWithCards = listOf(
    DeckWithCards(
        id = UUID.randomUUID().toString(),
        name = "Language design fundentals",
        cards = dummyCards
    ),
    DeckWithCards(
        id = UUID.randomUUID().toString(),
        name = "c programming",
        cards = dummyCards
    ),
    DeckWithCards(
        id = UUID.randomUUID().toString(),
        name = "nature of code",
        cards = dummyCards
    ),
    DeckWithCards(
        id = UUID.randomUUID().toString(),
        name = "humane software design",
        cards = dummyCards
    ),
    DeckWithCards(
        id = UUID.randomUUID().toString(),
        name = "anatomy",
        cards = dummyCards
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsSlideScreen(
    cardsSlide: NavDestination.CardsSlide,
    onNavigateBack: () -> Unit,
    onNavigateToDeckCardsSlideEditScreen: (data: NavDestination.DeckCardsSlideEdit) -> Unit
) {
    val decks = dummyDecksWithCards
    val decknames = decks.map { it.name }
    val cards = decks[0].cards
    val isFlippedMutState = remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { cards.size })
    val selectedFlashcardMutState = remember { mutableStateOf(cards[pagerState.currentPage]) }

    LaunchedEffect(pagerState.currentPage) {
        selectedFlashcardMutState.value = cards[pagerState.currentPage]
        isFlippedMutState.value = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
                title = {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Card Slide",
                            style = topAppBarTitleTextStyle
                        )
                        TopAppBarTitleDescriptionText(text = decknames.joinToString(separator = ", "))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_left),
                            contentDescription = "arrow back icon"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Gray50),
                verticalArrangement = Arrangement.spacedBy(
                    2.dp,
                    alignment = Alignment.CenterVertically
                )
            ) {
                item {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier
                                .widthIn(max = 300.dp)
                                .fillMaxWidth()
                        ) {
                            IconButton(onClick = {
                                val data =
                                    NavDestination.DeckCardsSlideEdit(
                                        selectedCardId = selectedFlashcardMutState.value.id
                                    )
                                onNavigateToDeckCardsSlideEditScreen(data)
                            }) {
                                Icon(
                                    painter = painterResource(R.drawable.pencil),
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                }
                item {
                    HorizontalPager(
                        state = pagerState,
                        pageSpacing = 8.dp,
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) { page ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FlashcardCard(
                                card = cards[page],
                                isFlipped = if (page == pagerState.currentPage) isFlippedMutState.value else false
                            )
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        FlashcardFlipButton(onClick = {
                            isFlippedMutState.value = !isFlippedMutState.value
                        })
                    }
                }
            }
        }
    }
}