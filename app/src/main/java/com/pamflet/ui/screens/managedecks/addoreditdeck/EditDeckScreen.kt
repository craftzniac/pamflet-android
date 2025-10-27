package com.pamflet.ui.screens.managedecks.addoreditdeck

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pamflet.ui.components.SimpleTopAppBar

@Composable
fun EditDeckScreen(
    onNavigateBack: () -> Unit,
    editDeckViewModel: EditDeckViewModel = viewModel()
) {
    Scaffold(
        topBar = { SimpleTopAppBar(title = "Edit Deck", onNavigateBack = onNavigateBack) }
    ) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    DeckForm(
                        deckName = editDeckViewModel.deckName,
                        updateDeckName = editDeckViewModel.updateDeckName,
                        onSubmit = {},
                        isEnableSubmitBtn = false,
                        submitBtnLabel = "Continue"
                    )
                }
            }
        }
    }
}
