package com.pamflet.ui.screens.managedecks.addoreditdeck

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pamflet.ui.components.SimpleTopAppBar
import com.pamflet.ui.screens.UpdateDeckActionStatus
import com.pamflet.ui.theme.Gray50

@Composable
fun EditDeckScreen(
    onNavigateBack: () -> Unit,
    editDeckViewModel: EditDeckViewModel
) {
    Scaffold(
        topBar = { SimpleTopAppBar(title = "Edit Deck", onNavigateBack = onNavigateBack) }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Gray50)
                .padding(contentPadding)
        ) {
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    DeckForm(
                        deckName = editDeckViewModel.deckName,
                        updateDeckName = editDeckViewModel.updateDeckName,
                        onSubmit = { editDeckViewModel.updateDeck() },
                        isEnableSubmitBtn = editDeckViewModel.deckName.isNotEmpty() || editDeckViewModel.updateDeckActionStatusMutState.value is UpdateDeckActionStatus.Submitting,
                        isSubmitting = editDeckViewModel.updateDeckActionStatusMutState.value is UpdateDeckActionStatus.Submitting,
                        submitBtnLabel = "Continue"
                    )
                }
            }
        }
    }
}
