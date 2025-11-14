package com.pamflet

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.pamflet.navigation.AppNavigation
import com.pamflet.shared.viewmodel.getSharedDecksViewModel
import com.pamflet.shared.ui.theme.PamfletTheme
import com.pamflet.shared.viewmodel.SharedDecksViewModel
import com.pamflet.shared.viewmodel.SharedUiEventViewModel
import com.pamflet.shared.ui.components.CustomDialog

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = (application as PamfletApplication)
        setContent {
            PamfletTheme(darkTheme = false) {
                App(app)
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App(app: PamfletApplication) {
    PamfletTheme(
        darkTheme = false
    ) {
        val snackBarHostState = remember { SnackbarHostState() }
        val sharedUiEventViewModel: SharedUiEventViewModel = viewModel()
        val sharedDecksViewModel: SharedDecksViewModel =
            getSharedDecksViewModel(app, sharedUiEventViewModel)

        LaunchedEffect(sharedUiEventViewModel.snackBarMessage) {
            if (sharedUiEventViewModel.snackBarMessage.isNotEmpty()) {
                snackBarHostState.showSnackbar(
                    sharedUiEventViewModel.snackBarMessage
                )
                sharedUiEventViewModel.clearSnackBarMessage()
            }
        }

        if (sharedUiEventViewModel.isDeleteDeckDialogOpen) {
            val deckIdAwaitingConfirmation = sharedDecksViewModel.deleteDeckAwaitingConfirmation
            if (deckIdAwaitingConfirmation != null) {
                CustomDialog(
                    title = "Delete deck",
                    description = "Are you sure you want to delete this deck?",
                    isSubmitting = sharedDecksViewModel.isDeletingDeckSubmitting(
                        deckIdAwaitingConfirmation
                    ),
                    onConfirm = {
                        sharedDecksViewModel.deleteDeck(deckIdAwaitingConfirmation)
                    },
                    onCancel = {
                        sharedDecksViewModel.removeDeleteDeckAwaitingConfirmation(
                            deckIdAwaitingConfirmation
                        )
                        sharedUiEventViewModel.closeDeleteDialog()
                    })
            }
        }

        val navController = rememberNavController()

        Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }, topBar = {}) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AppNavigation(navController, app, sharedUiEventViewModel, sharedDecksViewModel)
            }
        }
    }
}