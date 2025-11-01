package com.pamflet.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.toRoute
import com.pamflet.PamfletApplication
import com.pamflet.shared.ui.components.BottomNavBar
import com.pamflet.shared.viewmodel.SharedDecksViewModel
import com.pamflet.shared.viewmodel.SharedDecksViewModelFactory
import com.pamflet.features.auth.ui.LoginScreen
import com.pamflet.features.auth.ui.SignupScreen
import com.pamflet.features.deck.card.ui.DeckCardsListScreen
import com.pamflet.features.deck.card.ui.DeckCardsListViewModel
import com.pamflet.features.deck.card.ui.DeckCardsListViewModelFactory
import com.pamflet.features.deck.card.ui.EditCardScreen
import com.pamflet.features.deck.ui.AddDeckScreen
import com.pamflet.features.deck.ui.AddDeckViewModelFactory
import com.pamflet.features.deck.ui.EditDeckScreen
import com.pamflet.features.deck.ui.EditDeckViewModel
import com.pamflet.features.deck.ui.EditDeckViewModelFactory
import com.pamflet.features.deck.ui.ManageDecksScreen
import com.pamflet.features.deck.ui.ManageDecksViewModelFactory
import com.pamflet.features.profile.ui.ProfileScreen
import com.pamflet.features.review.ui.ReviewScreen
import com.pamflet.features.review.ui.SetupReviewScreen
import com.pamflet.features.review.ui.SetupReviewViewModelFactory

@Composable
fun AppNavigation(app: PamfletApplication) {
    val navController = rememberNavController()

    val currentSelectedRoute =
        navController.currentBackStackEntryAsState().value?.destination?.route
    val onNavigateToProfileScreen = {
        navController.navigate(route = NavDestination.Profile)
    }

    val onNavigateToCardsSlideSetupScreen = {
        navController.navigate(route = NavDestination.SetupReview)
    }

    val onNavigateToManageDecksScreen = {
        navController.navigate(route = NavDestination.ManageDecks)
    }

    val onNavigateToReviewScreen = { data: NavDestination.Review ->
        navController.navigate(
            route = NavDestination.Review(
                selectedDeckIds = data.selectedDeckIds,
                maxNumberOfCards = data.maxNumberOfCards,
                isShuffleCards = data.isShuffleCards
            )
        )
    }

    val onNavigateToEditCardScreen =
        { data: NavDestination.EditCard ->
            navController.navigate(route = data)
        }

    val onNavigateToDeckCardsListScreen = { data: NavDestination.DeckCardsList ->
        navController.navigate(route = data)
    }

    val onNavigateToAddDeckScreen =
        { navController.navigate(route = NavDestination.AddDeck) }

    val onNavigateToEditDeckScreen = { data: NavDestination.EditDeck ->
        navController.navigate(route = data)
    }

    val onNavigateBack: () -> Unit = {
        navController.popBackStack()
    }

    val bottomNavBar = @Composable {
        BottomNavBar(
            onNavigateToCardsSlideSetupScreen,
            onNavigateToManageDecksScreen,
            onNavigateToProfileScreen,
            currentSelectedRoute
        )
    }


    val navGraph = remember(navController) {
        navController.createGraph(
            startDestination = NavDestination.Login, route = NavDestination.Root::class
        ) {
            // Start Auth com.pamflet.ui.features
            composable<NavDestination.Login> {
                LoginScreen(
                    onNavigateToSignupScreen = {
                        navController.navigate(route = NavDestination.Signup)
                    },
                    onNavigateToCardsSlideSetupScreen,
                )
            }
            composable<NavDestination.Signup> {
                SignupScreen(
                    onNavigateToLoginScreen = {
                        navController.navigate(route = NavDestination.Login)
                    }, onNavigateToManageDecksScreen
                )
            }
            // End Auth com.pamflet.ui.features


            // Start flashcard usage com.pamflet.ui.features
            composable<NavDestination.SetupReview> { backStackEntry ->
                val sharedDecksViewModel: SharedDecksViewModel = viewModel(
                    factory = SharedDecksViewModelFactory(
                        deckRepository = app.deckRepository,
                        flashcardRepository = app.flashcardRepository
                    ),
                    viewModelStoreOwner = remember(backStackEntry) {
                        navController.getBackStackEntry(route = NavDestination.Root)
                    })
                SetupReviewScreen(
                    setupReviewViewModel = viewModel(
                        factory = SetupReviewViewModelFactory(
                            sharedDecksViewModel, deckRepository = app.deckRepository
                        )
                    ), bottomNavBar, onNavigateToReviewScreen
                )
            }
            composable<NavDestination.Review> { backStackEntry ->
                val reviewNavData: NavDestination.Review = backStackEntry.toRoute()
                ReviewScreen(
                    reviewNavData, onNavigateBack, onNavigateToEditCardScreen
                )
            }
            // End flashcard usage com.pamflet.ui.features


            // Start flashcard/deck management com.pamflet.ui.features
            composable<NavDestination.ManageDecks> { backStackEntry ->
                val sharedDecksViewModel: SharedDecksViewModel = viewModel(
                    factory = SharedDecksViewModelFactory(
                        app.deckRepository,
                        app.flashcardRepository
                    ),
                    viewModelStoreOwner = remember(backStackEntry) {
                        navController.getBackStackEntry(
                            route = NavDestination.Root
                        )
                    })

                ManageDecksScreen(
                    bottomNavBar, manageDecksViewModel = viewModel(
                        factory = ManageDecksViewModelFactory(sharedDecksViewModel)
                    ),
                    onNavigateToDeckCardsListScreen,
                    onNavigateToAddDeckScreen,
                    onNavigateToEditDeckScreen
                )
            }
            composable<NavDestination.EditCard> { backStackEntry ->
                val editCardNavData: NavDestination.EditCard =
                    backStackEntry.toRoute()
                EditCardScreen(editCardNavData, onNavigateBack)
            }
            composable<NavDestination.DeckCardsList> { backStackEntry ->
                val deckCardsListNavData: NavDestination.DeckCardsList =
                    backStackEntry.toRoute()

                val deckCardsListViewModel: DeckCardsListViewModel = viewModel(
                    factory = DeckCardsListViewModelFactory(
                        deckCardsListNavData,
                        flashcardRepository = app.flashcardRepository,
                        deckRepository = app.deckRepository
                    )
                )
                DeckCardsListScreen(
                    onNavigateBack,
                    onNavigateToEditCardScreen,
                    deckCardsListViewModel
                )
            }
            composable<NavDestination.AddDeck> {
                val sharedDecksViewModel: SharedDecksViewModel = viewModel(
                    viewModelStoreOwner = navController.getBackStackEntry(NavDestination.Root),
                    factory = SharedDecksViewModelFactory(
                        app.deckRepository,
                        app.flashcardRepository
                    )
                )

                AddDeckScreen(
                    onNavigateBack, addDeckViewModel = viewModel(
                        factory = AddDeckViewModelFactory(
                            sharedDecksViewModel = sharedDecksViewModel,
                            deckRepository = app.deckRepository
                        )
                    )
                )
            }
            composable<NavDestination.EditDeck> { backStackEntry ->
                val sharedDecksViewModel: SharedDecksViewModel = viewModel(
                    factory = SharedDecksViewModelFactory(
                        deckRepository = app.deckRepository,
                        flashcardRepository = app.flashcardRepository
                    ),
                    viewModelStoreOwner = navController.getBackStackEntry(NavDestination.Root)
                )

                val editDeckData: NavDestination.EditDeck = backStackEntry.toRoute()
                val editDeckViewModel: EditDeckViewModel = viewModel(
                    factory = EditDeckViewModelFactory(
                        editDeckData = editDeckData,
                        sharedDecksViewModel
                    )
                )
                EditDeckScreen(onNavigateBack, editDeckViewModel)
            }
            // End flashcard/deck management com.pamflet.ui.features


            // Start user profile management
            composable<NavDestination.Profile> { ProfileScreen(bottomNavBar) }
            // End user profile management
        }
    }

    NavHost(navController = navController, graph = navGraph)
}