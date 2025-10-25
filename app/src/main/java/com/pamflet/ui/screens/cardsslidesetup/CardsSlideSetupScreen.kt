package com.pamflet.ui.screens.cardsslidesetup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pamflet.NavDestination
import com.pamflet.ui.components.NumberStepInput
import com.pamflet.ui.components.SimpleTopAppBar
import com.pamflet.ui.theme.Gray200
import com.pamflet.ui.theme.Gray50
import com.pamflet.ui.theme.Gray500
import com.pamflet.ui.theme.Gray600
import com.pamflet.ui.theme.Gray900
import com.pamflet.ui.theme.Purple400
import com.pamflet.ui.theme.Purple900
import com.pamflet.ui.theme.Red500

val sectionHeaderTextStyle = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.Medium,
    color = Purple900
)

@Composable
fun CardsSlideSetupScreen(
    cardsSlideSetupViewModel: CardsSlideSetupViewModel,
    bottomNavBar: @Composable () -> Unit,
    onNavigateToCardsSlideScreen: (data: NavDestination.CardsSlide) -> Unit
) {
    Scaffold(
        topBar = { SimpleTopAppBar(title = "Card Slide Setup", isShowPamfletLogo = true) },
        bottomBar = bottomNavBar
    ) { paddingValues ->
        val decksUiState = cardsSlideSetupViewModel.decksUiState
        val maxNumberOfCardsMutState = remember { mutableIntStateOf(100) }
        val isShuffleCardsMutState = remember { mutableStateOf(false) }
        val isShuffleCards = cardsSlideSetupViewModel.isShuffleCards
        val selectedDeckIds = cardsSlideSetupViewModel.selectedDeckIds
        val maxNumberOfCards = cardsSlideSetupViewModel.maxNumberOfCards

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Gray50)
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                style = sectionHeaderTextStyle,
                                text = "Choose Decks",
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "${
                                    if (decksUiState is DecksUiState.Success) decksUiState.decks.size else ""
                                }", fontSize = 16.sp, color = Purple900
                            )
                        }

                        when (decksUiState) {
                            is DecksUiState.Loading -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.width(32.dp),
                                        color = Gray600,
                                        trackColor = Gray200
                                    )
                                }
                            }

                            is DecksUiState.Success -> {
                                val decks = decksUiState.decks

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                ) {
                                    decks.forEach { deck ->
                                        val roundedShape = RoundedCornerShape(percent = 100)
                                        val isSelected =
                                            cardsSlideSetupViewModel.isDeckSelected(deck.id)
                                        SuggestionChip(
                                            enabled = true,
                                            border = SuggestionChipDefaults.suggestionChipBorder(
                                                enabled = true,
                                                borderColor = Gray200,
                                                disabledBorderColor = Color.Unspecified,
                                                borderWidth = 1.dp,
                                            ),
                                            onClick = {
                                                cardsSlideSetupViewModel.toggleDeckSelection(
                                                    deckId = deck.id
                                                )
                                            },
                                            label = {
                                                Text(
                                                    deck.name,
                                                    modifier = Modifier.padding(10.dp)
                                                )
                                            },
                                            shape = roundedShape,
                                            colors = SuggestionChipDefaults.suggestionChipColors(
                                                containerColor = if (isSelected) Purple400 else Color.Transparent,
                                                labelColor = if (isSelected) Color.White else Gray900
                                            )
                                        )
                                    }
                                }
                            }

                            is DecksUiState.Error -> {
                                Text("An Error occurred", color = Red500)
                            }
                        }
                    }
                }

                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Max. number of Cards",
                            style = sectionHeaderTextStyle,
                            modifier = Modifier.weight(1f)
                        )
                        NumberStepInput(
                            value = maxNumberOfCards,
                            onIncrement = { cardsSlideSetupViewModel.incrementMaxNumberOfCards() },
                            onDecrement = { cardsSlideSetupViewModel.decrementMaxNumberOfCards() },
                            rawUpdate = { rawString ->
                                cardsSlideSetupViewModel.rawUpdateMaxNumberOfCards(
                                    rawString
                                )
                            }
                        )
                    }
                }

                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Shuffle Cards",
                            style = sectionHeaderTextStyle,
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = isShuffleCards,
                            onCheckedChange = cardsSlideSetupViewModel.setIsShuffleCards
                        )
                    }
                }

                item {
                    Button(
                        enabled = selectedDeckIds.isNotEmpty(),
                        onClick = {
                            onNavigateToCardsSlideScreen(
                                NavDestination.CardsSlide(
                                    maxNumberOfCards = maxNumberOfCards,
                                    isShuffleCards = isShuffleCards,
                                    selectedDeckIds = selectedDeckIds
                                )
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text(
                            "Begin Card Slide",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(4.dp),
                        )
                    }
                }
            }
        }
    }
}