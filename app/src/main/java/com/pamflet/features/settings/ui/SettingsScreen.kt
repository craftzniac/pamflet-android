package com.pamflet.features.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pamflet.features.review.ui.sectionHeaderTextStyle
import com.pamflet.shared.ui.components.CustomDialog
import com.pamflet.shared.ui.components.LoadingSpinner
import com.pamflet.shared.ui.components.PButton
import com.pamflet.shared.ui.components.PButtonVariant
import com.pamflet.shared.ui.components.SimpleTopAppBar
import com.pamflet.shared.ui.theme.Gray50
import com.pamflet.shared.ui.theme.Red500

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
    bottomNavBar: @Composable () -> Unit
) {
    LaunchedEffect(settingsViewModel.deleteAllDecksActionStatus) {
        when (settingsViewModel.deleteAllDecksActionStatus.value) {
            is DeleteAllDecksActionStatus.Success, is DeleteAllDecksActionStatus.Error -> {
                settingsViewModel.resetDeleteAllActionStatus()
            }

            else -> {}
        }
    }

    Scaffold(
        topBar = { SimpleTopAppBar(title = "Settings") },
        bottomBar = bottomNavBar
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Gray50)
                    .padding(16.dp)
            ) {
                val isSubmitting =
                    settingsViewModel.deleteAllDecksActionStatus.value is DeleteAllDecksActionStatus.Submitting
                if (settingsViewModel.isDeleteAllDeckDialogOpen) {
                    DeleteAllDecksDialog(
                        continueDelete = { settingsViewModel.deleteAll() },
                        cancelDelete = { settingsViewModel.closeDeleteDialog() },
                        isSubmitting = isSubmitting
                    )
                }
                LazyColumn(
                    Modifier
                        .background(Color.Transparent)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                style = sectionHeaderTextStyle,
                                text = "Clear Data",
                            )
                            PButton(
                                enabled = !isSubmitting,
                                onClick = { settingsViewModel.openDeleteDialog() },
                                variant = PButtonVariant.Outline,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = Red500
                                )
                            ) {
                                if (isSubmitting) {
                                    LoadingSpinner()
                                } else {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(
                                            4.dp,
                                            Alignment.CenterHorizontally
                                        )
                                    ) {
                                        Icon(
                                            painter = painterResource(com.pamflet.R.drawable.trash_can),
                                            contentDescription = "trash can",
                                        )
                                        Text("Delete All Decks")
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
fun DeleteAllDecksDialog(
    continueDelete: () -> Unit,
    cancelDelete: () -> Unit,
    isSubmitting: Boolean
) {
    CustomDialog(
        title = "Delete all decks",
        description = "Are you sure? This will wipe all your decks and cards",
        onConfirm = continueDelete,
        onCancel = cancelDelete,
        isSubmitting = isSubmitting
    )
}