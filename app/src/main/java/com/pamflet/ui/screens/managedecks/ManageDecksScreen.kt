package com.pamflet.ui.screens.managedecks

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
import com.pamflet.R
import com.pamflet.ui.components.LoadingSpinner
import com.pamflet.ui.components.SimpleTopAppBar
import com.pamflet.ui.screens.DecksUiState
import com.pamflet.ui.screens.cardsslide.dummyDecksWithCards
import com.pamflet.ui.theme.Gray50
import com.pamflet.ui.theme.Gray600
import com.pamflet.ui.theme.Gray900
import androidx.compose.runtime.getValue

@Composable
fun ManageDecksScreen(
    bottomNavBar: @Composable () -> Unit,
    manageDecksViewModel: ManageDecksViewModel,
    onNavigateToDeckCardsListScreen: (data: NavDestination.DeckCardsList) -> Unit,
    onNavigateToAddDeckScreen: () -> Unit,
) {
    val decksUiState by manageDecksViewModel.decksUiStateMutState
    Scaffold(
        topBar = { SimpleTopAppBar(title = "Manage Decks") },
        bottomBar = bottomNavBar
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
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
                            onClick = onNavigateToAddDeckScreen,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.plus),
                                contentDescription = "",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Deck")
                        }
                    }
                }

                item {
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
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text((decksUiState as DecksUiState.Error).message, fontSize = 16.sp, color = Gray900)
                                Button(onClick = {}, modifier = Modifier.height(48.dp)) {
                                    Text("Retry")
                                }
                            }
                        }

                        is DecksUiState.Success -> {
                            val decks = (decksUiState as DecksUiState.Success).decks
                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 16.dp
                                )
                            ) {
                                decks.forEach { deck ->
                                    Card(
                                        shape = RoundedCornerShape(8.dp),
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                val deckCardsListData =
                                                    NavDestination.DeckCardsList(
                                                        selectedDeckId = deck.id
                                                    )
                                                onNavigateToDeckCardsListScreen(deckCardsListData)
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
                                                text = "${deck.cardCount}",
                                                fontSize = 12.sp,
                                                color = Gray600
                                            )
                                            Text(
                                                text = deck.name,
                                                fontSize = 16.sp,
                                                color = Gray900,
                                                fontWeight = FontWeight.Medium,
                                                modifier = Modifier.weight(1F),
                                                overflow = TextOverflow.Ellipsis,
                                                maxLines = 1
                                            )
                                            IconButton(onClick = {}) {
                                                Icon(
                                                    imageVector = Icons.Default.MoreVert,
                                                    contentDescription = "overflow",
                                                    tint = Gray900
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