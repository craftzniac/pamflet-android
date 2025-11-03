package com.pamflet.features.deck.card.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.pamflet.core.domain.Deck
import com.pamflet.shared.ui.components.ErrorSection
import com.pamflet.shared.ui.components.LoadingSpinner
import com.pamflet.shared.ui.theme.Gray100
import com.pamflet.shared.ui.theme.Gray200
import com.pamflet.shared.ui.theme.Gray300
import com.pamflet.shared.ui.theme.Gray50
import com.pamflet.shared.ui.theme.Gray700
import com.pamflet.shared.ui.theme.Gray900
import com.pamflet.shared.viewmodel.DecksUiState

@Composable
fun DeckSelectDialogTrigger(
    openDialog: () -> Unit,
    selectedDeck: Deck?
) {

    val interactionSource = remember { MutableInteractionSource() }
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Press) {
                openDialog()
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = true, onClick = {
                openDialog()
            })
    ) {
        Text("Choose a deck", fontSize = 16.sp, color = Gray700)
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = selectedDeck?.name ?: "",
            readOnly = true,
            placeholder = { Text("Select ...") },
            maxLines = 1,
            onValueChange = {},
            trailingIcon = {
                Icon(
                    painter = painterResource(com.pamflet.R.drawable.keyboard_arrow_down),
                    contentDescription = "",
                    tint = Gray900,
                    modifier = Modifier.size(24.dp)
                )
            },
            interactionSource = interactionSource
        )
    }
}

@Composable
fun DeckSelectDialog(
    onDismissRequest: () -> Unit,
    decksUiState: DecksUiState,
    retryFetchDecks: () -> Unit,
    updateSelectedDeck: (Deck) -> Unit,
    onNavigateToAddDeckScreen: () -> Unit,
    selectedDeck: Deck?
) {
    Dialog(onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .height(300.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Gray50)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Gray100)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        "Decks",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    IconButton(
                        onClick = { onNavigateToAddDeckScreen() },
                        colors = IconButtonDefaults.iconButtonColors(contentColor = Gray900)
                    ) {
                        Icon(
                            painter = painterResource(com.pamflet.R.drawable.plus),
                            contentDescription = ""
                        )
                    }
                }
                when (decksUiState) {
                    is DecksUiState.Error -> {
                        ErrorSection(
                            message = "Couldn't fetch decks",
                            onAction = retryFetchDecks
                        )
                    }

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

                    is DecksUiState.Success -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            decksUiState.decks.forEach { deck ->
                                item {
                                    Box(
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp,
                                            vertical = 4.dp
                                        )
                                    ) {
                                        TextButton(
                                            onClick = {
                                                updateSelectedDeck(deck)
                                                onDismissRequest()
                                            },
                                            colors = ButtonDefaults.textButtonColors(
                                                contentColor = Gray900,
                                                containerColor = if (deck.id === selectedDeck?.id) Gray200 else Color.Transparent
                                            ),
                                            content = {
                                                Text(
                                                    deck.name,
                                                    fontSize = 18.sp,
                                                    modifier = Modifier.fillMaxWidth(),
                                                    textAlign = TextAlign.Start
                                                )
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(48.dp),
                                            shape = RoundedCornerShape(8.dp)
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