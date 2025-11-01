package com.pamflet.features.review.ui

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
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pamflet.navigation.NavDestination
import com.pamflet.shared.ui.components.ErrorSection
import com.pamflet.shared.ui.components.LoadingSpinner
import com.pamflet.shared.ui.components.NumberStepInput
import com.pamflet.shared.ui.components.SimpleTopAppBar
import com.pamflet.shared.ui.theme.Gray200
import com.pamflet.shared.ui.theme.Gray50
import com.pamflet.shared.ui.theme.Gray900
import com.pamflet.shared.ui.theme.Purple400
import com.pamflet.shared.ui.theme.Purple900
import com.pamflet.shared.viewmodel.DecksUiState

val sectionHeaderTextStyle = TextStyle(
    fontSize = 16.sp,
    fontWeight = FontWeight.Medium,
    color = Purple900
)

@Composable
fun SetupReviewScreen(
    setupReviewViewModel: SetupReviewViewModel,
    bottomNavBar: @Composable () -> Unit,
    onNavigateToReviewScreen: (data: NavDestination.Review) -> Unit
) {
    Scaffold(
        topBar = { SimpleTopAppBar(title = "Card Slide Setup", isShowPamfletLogo = true) },
        bottomBar = bottomNavBar
    ) { paddingValues ->
        val decksUiState by setupReviewViewModel.decksUiStateMutState
        val maxNumberOfCardsMutState = remember { mutableIntStateOf(100) }
        val isShuffleCardsMutState = remember { mutableStateOf(false) }
        val isShuffleCards = setupReviewViewModel.isShuffleCards
        val selectedDeckIds = setupReviewViewModel.selectedDeckIds
        val maxNumberOfCards = setupReviewViewModel.maxNumberOfCards

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
                                    if (decksUiState is DecksUiState.Success) (decksUiState as DecksUiState.Success).decks.size else ""
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
                                    LoadingSpinner()
                                }
                            }

                            is DecksUiState.Error -> {
                                ErrorSection(
                                    message = (decksUiState as DecksUiState.Error).message
                                )
                            }

                            is DecksUiState.Success -> {
                                val decks = (decksUiState as DecksUiState.Success).decks

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                ) {
                                    decks.forEach { deck ->
                                        val roundedShape = RoundedCornerShape(percent = 100)
                                        val isSelected =
                                            setupReviewViewModel.isDeckSelected(deck.id)
                                        SuggestionChip(
                                            enabled = true,
                                            border = SuggestionChipDefaults.suggestionChipBorder(
                                                enabled = true,
                                                borderColor = Gray200,
                                                disabledBorderColor = Color.Unspecified,
                                                borderWidth = 1.dp,
                                            ),
                                            onClick = {
                                                setupReviewViewModel.toggleDeckSelection(
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
                            onIncrement = { setupReviewViewModel.incrementMaxNumberOfCards() },
                            onDecrement = { setupReviewViewModel.decrementMaxNumberOfCards() },
                            rawUpdate = { rawString ->
                                setupReviewViewModel.rawUpdateMaxNumberOfCards(
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
                            onCheckedChange = setupReviewViewModel.setIsShuffleCards
                        )
                    }
                }

                item {
                    Button(
                        enabled = selectedDeckIds.isNotEmpty(),
                        onClick = {
                            (
                                NavDestination.Review(
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