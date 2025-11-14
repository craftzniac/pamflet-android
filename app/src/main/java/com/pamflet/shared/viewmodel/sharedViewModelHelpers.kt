package com.pamflet.shared.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.pamflet.PamfletApplication
import com.pamflet.core.data.repository.FlashcardRepository
import com.pamflet.core.domain.DeleteDeckUseCase
import com.pamflet.navigation.NavDestination

//@Composable
//fun getSharedDecksViewModel(
//    app: PamfletApplication, backStackEntry: NavBackStackEntry, navController: NavController
//): SharedDecksViewModel {
//    val owner = remember(backStackEntry) { navController.getBackStackEntry(NavDestination.Root) }
//    return viewModel(
//        factory = SharedDecksViewModelFactory(
//            deckRepository = app.deckRepository,
//            deleteDeckUseCase = DeleteDeckUseCase(app.deckRepository, app.flashcardRepository)
//        ), viewModelStoreOwner = owner
//    ) as SharedDecksViewModel
//}


// Only call this once at the top of the root composable so that it is scoped to the activity, then reuse reference
@Composable
fun getSharedDecksViewModel(
    app: PamfletApplication,
    sharedUiEventViewModel: SharedUiEventViewModel
): SharedDecksViewModel {
    return viewModel(
        factory = SharedDecksViewModelFactory(
            deckRepository = app.deckRepository,
            deleteDeckUseCase = DeleteDeckUseCase(app.deckRepository, app.flashcardRepository),
            sharedUiEventViewModel = sharedUiEventViewModel
        )
    ) as SharedDecksViewModel
}

@Composable
fun getSharedCardListViewModel(
    flashcardRepository: FlashcardRepository,
    deckId: String,
    backStackEntry: NavBackStackEntry,
    navController: NavController
): SharedCardListViewModel {
    val owner = remember(backStackEntry) { navController.getBackStackEntry(NavDestination.Root) }
    return viewModel(
        factory = SharedCardListViewModelFactory(flashcardRepository), viewModelStoreOwner = owner
    ) as SharedCardListViewModel
}

