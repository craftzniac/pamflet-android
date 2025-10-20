package com.pamflet.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pamflet.NavDestination
import com.pamflet.decks
import com.pamflet.ui.theme.Gray50
import com.pamflet.ui.theme.Gray600
import com.pamflet.ui.theme.Gray900
import com.pamflet.ui.theme.Red500

@Composable
fun DeckCardsListScreen(
    deckCardsList: NavDestination.DeckCardsList,
    onNavigateBack: () -> Unit,
    onNavigateToDeckCardsSlideEditScreen: (data: NavDestination.DeckCardsSlideEdit) -> Unit
) {
    val deck = decks[0]
    val cards = deck.cards
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
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    ) {
                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Icon(
                                painter = painterResource(com.pamflet.R.drawable.plus),
                                contentDescription = "",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Card")
                        }
                    }
                }

                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    ) {
                        cards.forEachIndexed { index, card ->
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        val deckCardsSlideEdit = NavDestination.DeckCardsSlideEdit(
                                            selectedCardId = card.id
                                        )
                                        onNavigateToDeckCardsSlideEditScreen(deckCardsSlideEdit)
                                    },
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                                            painter = painterResource(com.pamflet.R.drawable.trash_can),
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