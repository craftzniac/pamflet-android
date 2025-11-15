package com.pamflet.features.review.ui

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
import com.pamflet.shared.ui.components.PreviewCard
import com.pamflet.navigation.NavDestination
import com.pamflet.R
import com.pamflet.shared.ui.components.FlashcardFlipButton
import com.pamflet.shared.ui.components.topAppBarTitleTextStyle
import com.pamflet.shared.ui.theme.Gray50
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.pamflet.shared.ui.components.ErrorSection
import com.pamflet.shared.ui.components.FullscreenLoadingSpinner
import com.pamflet.shared.ui.components.PButton
import com.pamflet.shared.ui.components.PButtonVariant
import com.pamflet.shared.ui.theme.Gray600

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    reviewViewModel: ReviewViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToEditCardScreen: (data: NavDestination.EditCard) -> Unit
) {
    val flashCardsUiState by reviewViewModel.flashcardsUiState
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
                            text = "Cards Review",
                            style = topAppBarTitleTextStyle
                        )
//                        TopAppBarTitleDescriptionText(text = decknames.joinToString(separator = ", "))
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
                .fillMaxSize()
                .background(color = Gray50),
            contentAlignment = Alignment.Center,
        ) {
            when (flashCardsUiState) {
                is FlashcardsUiState.Loading -> {
                    FullscreenLoadingSpinner()
                }

                is FlashcardsUiState.Error -> {
                    ErrorSection(
                        message = (flashCardsUiState as FlashcardsUiState.Error).message,
                        onAction = { reviewViewModel.retryFetchFlashcards() }
                    )
                }

                is FlashcardsUiState.Success -> {
                    val cards = (flashCardsUiState as FlashcardsUiState.Success).flashcards
                    if (cards.isEmpty()) {
                        ReviewScreenEmpty(onNavigateBack)
                    } else {
                        val pagerState = rememberPagerState(pageCount = { cards.size })
                        val selectedFlashcardMutState =
                            remember { mutableStateOf(cards[pagerState.currentPage]) }

                        LaunchedEffect(pagerState.currentPage) {
                            selectedFlashcardMutState.value = cards[pagerState.currentPage]
                            reviewViewModel.updateIsFlipped(false)
                        }

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = Color.Transparent),
                            verticalArrangement = Arrangement.spacedBy(
                                2.dp,
                                alignment = Alignment.CenterVertically
                            )
                        ) {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.End,
                                        modifier = Modifier
                                            .widthIn(max = 300.dp)
                                            .fillMaxWidth()
                                    ) {
                                        IconButton(onClick = {
                                            val data =
                                                NavDestination.EditCard(
                                                    selectedCardId = selectedFlashcardMutState.value.id,
                                                    deckId = selectedFlashcardMutState.value.deckId
                                                )
                                            onNavigateToEditCardScreen(data)
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
                                        val card = cards[page]
                                        PreviewCard(
                                            cardFrontContent = card.front,
                                            cardBackContent = card.back,
                                            isFlipped = if (page == pagerState.currentPage) reviewViewModel.isFlippedState.value else false
                                        )
                                    }
                                }
                            }
                            item {
                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 0.dp, vertical = 8.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    FlashcardFlipButton(onClick = {
                                        reviewViewModel.toggleIsFlipped()
                                    })
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
fun ReviewScreenEmpty(onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(
            16.dp, alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ErrorSection(
            message = "Specified deck(s) are empty",
            onAction = onNavigateBack,
            actionLabel = "Go back"
        )
    }
}
