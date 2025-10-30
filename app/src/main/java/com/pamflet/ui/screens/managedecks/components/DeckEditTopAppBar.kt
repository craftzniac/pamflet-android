package com.pamflet.ui.screens.managedecks.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.pamflet.R
import com.pamflet.ui.components.TopAppBarTitleDescriptionText
import com.pamflet.ui.components.topAppBarTitleTextStyle
import com.pamflet.ui.screens.managedecks.deckcardslist.DeckUiState
import com.pamflet.ui.theme.Gray600
import com.pamflet.ui.theme.Gray900
import com.pamflet.ui.theme.Red200
import com.pamflet.ui.theme.Red500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckEditTopAppBar(
    deckUiState: DeckUiState, onNavigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            Column(
                modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Deck", style = topAppBarTitleTextStyle
                )
                if (deckUiState is DeckUiState.Success && deckUiState.deck != null) {
                    TopAppBarTitleDescriptionText(deckUiState.deck.name)
                }
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
        actions = {
            IconButton(
                onClick = {},
                enabled = deckUiState is DeckUiState.Success && deckUiState.deck != null,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Gray900,
                    disabledContentColor = Gray600
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.pencil),
                    contentDescription = "pencil",
                )
            }
            IconButton(
                onClick = {},
                enabled = deckUiState is DeckUiState.Success && deckUiState.deck != null,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Red500,
                    disabledContentColor = Red200
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.trash_can),
                    contentDescription = "trash can",
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
    )
}
