package com.pamflet.features.deck.ui

import android.util.Log
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pamflet.navigation.NavDestination
import com.pamflet.R
import com.pamflet.shared.ui.components.LoadingSpinner
import com.pamflet.shared.ui.components.SimpleTopAppBar
import com.pamflet.shared.ui.theme.Gray50
import com.pamflet.shared.ui.theme.Gray600
import com.pamflet.shared.ui.theme.Gray900
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.pamflet.shared.ui.components.ErrorSection
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import com.pamflet.core.domain.Deck
import com.pamflet.shared.ui.components.CustomDialog
import com.pamflet.shared.ui.theme.Gray100
import com.pamflet.shared.ui.theme.Red500
import com.pamflet.shared.viewmodel.DecksUiState
import com.pamflet.shared.viewmodel.DeleteDeckActionStatus

@Composable
fun ManageDecksScreen(
    bottomNavBar: @Composable () -> Unit,
    manageDecksViewModel: ManageDecksViewModel,
    onNavigateToDeckCardsListScreen: (data: NavDestination.CardList) -> Unit,
    onNavigateToAddDeckScreen: () -> Unit,
    onNavigateToEditDeckScreen: (data: NavDestination.EditDeck) -> Unit,
) {
    val decksUiState by manageDecksViewModel.decksUiStateMutState
    Scaffold(
        topBar = { SimpleTopAppBar(title = "Manage Decks") }, bottomBar = bottomNavBar
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .background(color = Gray50)
        ) {
            // custom dialog used to be here
            when (decksUiState) {
                is DecksUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Transparent)
                            .padding(16.dp), contentAlignment = Alignment.Center
                    ) {
                        LoadingSpinner()
                    }
                }

                is DecksUiState.Error -> {
                    ErrorSection(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Transparent),
                        message = (decksUiState as DecksUiState.Error).message,
                        onAction = { manageDecksViewModel.fetchDecks() })
                }

                is DecksUiState.Success -> {
                    val decks = (decksUiState as DecksUiState.Success).decks
                    if (decks.isEmpty()) {
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
                                "You currently have no decks",
                                fontSize = 20.sp,
                                color = Gray600,
                                textAlign = TextAlign.Center
                            )
                            CreateDeckButton(onClick = onNavigateToAddDeckScreen)
                        }
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
                                    CreateDeckButton(onClick = onNavigateToAddDeckScreen)
                                }
                            }

                            item {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(
                                        start = 16.dp, end = 16.dp, bottom = 16.dp
                                    )
                                ) {
                                    decks.forEach { deck ->
                                        val isDeletingDeck =
                                            manageDecksViewModel.isDeletingDeckSubmitting(deck.id)
                                        Card(
                                            shape = RoundedCornerShape(8.dp),
                                            colors = CardDefaults.cardColors(containerColor = Color.White),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    if (!isDeletingDeck) {
                                                        val deckCardsListData =
                                                            NavDestination.CardList(
                                                                selectedDeckId = deck.id
                                                            )
                                                        onNavigateToDeckCardsListScreen(
                                                            deckCardsListData
                                                        )
                                                    }
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

                                                var expanded by remember { mutableStateOf(false) }
                                                Box {
                                                    IconButton(onClick = { expanded = !expanded }) {
                                                        Icon(
                                                            imageVector = Icons.Default.MoreVert,
                                                            contentDescription = "overflow button",
                                                            tint = Gray900
                                                        )
                                                    }

                                                    DropdownMenu(
                                                        expanded = expanded,
                                                        onDismissRequest = { expanded = false },
                                                        containerColor = Gray100,
                                                        modifier = Modifier.width(200.dp)
                                                    ) {
                                                        DropdownMenuItem(onClick = {
                                                            onNavigateToEditDeckScreen(
                                                                NavDestination.EditDeck(deckId = deck.id)
                                                            )
                                                            expanded = false
                                                        }, text = {
                                                            Row(
                                                                horizontalArrangement = Arrangement.spacedBy(
                                                                    8.dp
                                                                ),
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                Icon(
                                                                    painter = painterResource(
                                                                        R.drawable.pencil
                                                                    ),
                                                                    contentDescription = "",
                                                                    modifier = Modifier.size(24.dp),
                                                                    tint = Gray900
                                                                )
                                                                Text(
                                                                    "Edit",
                                                                    color = Gray900,
                                                                    fontSize = 16.sp
                                                                )
                                                            }
                                                        })
                                                        DropdownMenuItem(onClick = {
                                                            manageDecksViewModel.triggerDeckDelete(
                                                                deck.id
                                                            )
                                                            expanded = false
                                                        }, text = {
                                                            Row(
                                                                horizontalArrangement = Arrangement.spacedBy(
                                                                    8.dp
                                                                ),
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                Icon(
                                                                    painter = painterResource(
                                                                        R.drawable.trash_can
                                                                    ),
                                                                    contentDescription = "",
                                                                    modifier = Modifier.size(24.dp),
                                                                    tint = Red500
                                                                )
                                                                Text(
                                                                    "Delete",
                                                                    color = Red500,
                                                                    fontSize = 16.sp
                                                                )
                                                            }
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
                }
            }
        }
    }
}


@Composable
fun CreateDeckButton(onClick: () -> Unit) {
    Button(
        onClick, shape = RoundedCornerShape(8.dp), modifier = Modifier.height(48.dp)
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