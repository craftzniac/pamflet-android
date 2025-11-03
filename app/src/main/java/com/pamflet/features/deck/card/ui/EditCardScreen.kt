package com.pamflet.features.deck.card.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.pamflet.shared.ui.components.SimpleTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCardScreen(
    editCardViewModel: EditCardViewModel,
    onNavigateBack: () -> Unit
) {
    val cardUiState = editCardViewModel.cardUiState
    Scaffold(
        topBar = {
            SimpleTopAppBar(title = "Edit Card", actions = {
                IconButton(onClick = { }) {
                    Icon(
                        painter = painterResource(com.pamflet.R.drawable.trash_can),
                        contentDescription = "trash can"
                    )
                }
            })
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            Text("Edit card screen")
        }
    }
}