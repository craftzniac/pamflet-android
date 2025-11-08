package com.pamflet.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.toRoute
import com.pamflet.PamfletApplication
import com.pamflet.core.data.repository.FlashcardRepository
import com.pamflet.shared.ui.components.BottomNavBar
import com.pamflet.shared.viewmodel.SharedDecksViewModel
import com.pamflet.shared.viewmodel.SharedDecksViewModelFactory
import com.pamflet.features.auth.ui.LoginScreen
import com.pamflet.features.auth.ui.SignupScreen
import com.pamflet.features.deck.card.ui.AddCardScreen
import com.pamflet.features.deck.card.ui.AddCardViewModel
import com.pamflet.features.deck.card.ui.AddCardViewModelFactory
import com.pamflet.features.deck.card.ui.CardListScreen
import com.pamflet.features.deck.card.ui.CardListViewModel
import com.pamflet.features.deck.card.ui.DeckCardListViewModelFactory
import com.pamflet.features.deck.card.ui.EditCardScreen
import com.pamflet.features.deck.card.ui.EditCardViewModel
import com.pamflet.features.deck.card.ui.EditCardViewModelFactory
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
import com.pamflet.shared.viewmodel.SharedCardListViewModel
import com.pamflet.shared.viewmodel.SharedCardListViewModelFactory
import com.pamflet.shared.viewmodel.SharedUiEventViewModel


@Composable
fun getSharedDecksViewModel(
    app: PamfletApplication,
    backStackEntry: NavBackStackEntry,
    navController: NavController
): SharedDecksViewModel {
    val owner = remember(backStackEntry) { navController.getBackStackEntry(NavDestination.Root) }
    return viewModel(
        factory = SharedDecksViewModelFactory(
            deckRepository = app.deckRepository,
            flashcardRepository = app.flashcardRepository
        ),
        viewModelStoreOwner = owner
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
        factory = SharedCardListViewModelFactory(flashcardRepository),
        viewModelStoreOwner = owner
    ) as SharedCardListViewModel
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavigation(app: PamfletApplication) {
    val snackBarHostState = remember { SnackbarHostState() }
    val sharedUiEventViewModel: SharedUiEventViewModel = viewModel()

    LaunchedEffect(sharedUiEventViewModel.snackBarMessage) {
        if (sharedUiEventViewModel.snackBarMessage.isNotEmpty()) {
            snackBarHostState.showSnackbar(
                sharedUiEventViewModel.snackBarMessage
            )
            sharedUiEventViewModel.clearSnackBarMessage()
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }, topBar = {}) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
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

            val onNavigateToDeckCardsListScreen = { data: NavDestination.CardList ->
                navController.navigate(route = data)
            }

            val onNavigateToAddDeckScreen =
                { navController.navigate(route = NavDestination.AddDeck) }

            val onNavigateToEditDeckScreen = { data: NavDestination.EditDeck ->
                navController.navigate(route = data)
            }

            val onNavigateToAddCardScreen = { data: NavDestination.AddCard ->
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
                    startDestination = NavDestination.SetupReview,
                    route = NavDestination.Root::class
                ) {
                    // Start Auth features
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
                    // End Auth features


                    // Start flashcard usage features
                    composable<NavDestination.SetupReview> { backStackEntry ->
                        SetupReviewScreen(
                            setupReviewViewModel = viewModel(
                                factory = SetupReviewViewModelFactory(
                                    sharedDecksViewModel = getSharedDecksViewModel(
                                        app,
                                        backStackEntry,
                                        navController
                                    ),
                                    deckRepository = app.deckRepository
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
                    // End flashcard usage features


                    // Start flashcard/deck management features
                    composable<NavDestination.ManageDecks> { backStackEntry ->
                        ManageDecksScreen(
                            bottomNavBar, manageDecksViewModel = viewModel(
                                factory = ManageDecksViewModelFactory(
                                    sharedDecksViewModel = getSharedDecksViewModel(
                                        app,
                                        backStackEntry,
                                        navController
                                    ),
                                )
                            ),
                            onNavigateToDeckCardsListScreen,
                            onNavigateToAddDeckScreen,
                            onNavigateToEditDeckScreen
                        )
                    }
                    composable<NavDestination.EditCard> { backStackEntry ->

                        val editCardNavData: NavDestination.EditCard =
                            backStackEntry.toRoute()

                        val sharedCardListViewModel: SharedCardListViewModel =
                            getSharedCardListViewModel(
                                flashcardRepository = app.flashcardRepository,
                                deckId = editCardNavData.deckId,
                                backStackEntry,
                                navController
                            )

                        val editCardViewModel: EditCardViewModel = viewModel(
                            factory = EditCardViewModelFactory(
                                editCardNavData,
                                flashcardRepository = app.flashcardRepository,
                                sharedDecksViewModel = getSharedDecksViewModel(
                                    app,
                                    backStackEntry,
                                    navController
                                ),
                                sharedUiEventViewModel,
                                refetchCards = { sharedCardListViewModel.refetchCards() }
                            )
                        )
                        EditCardScreen(editCardViewModel, onNavigateBack, onNavigateToAddDeckScreen)
                    }
                    composable<NavDestination.CardList> { backStackEntry ->
                        val cardListNavData: NavDestination.CardList =
                            backStackEntry.toRoute()

                        val sharedCardListViewModel: SharedCardListViewModel =
                            getSharedCardListViewModel(
                                flashcardRepository = app.flashcardRepository,
                                deckId = cardListNavData.selectedDeckId,
                                backStackEntry,
                                navController
                            )

                        val cardListViewModel: CardListViewModel = viewModel(
                            factory = DeckCardListViewModelFactory(
                                flashcardRepository = app.flashcardRepository,
                                deckRepository = app.deckRepository,
                                sharedCardListViewModel
                            )
                        )

                        CardListScreen(
                            cardListNavData,
                            onNavigateBack,
                            onNavigateToAddCardScreen,
                            onNavigateToEditCardScreen,
                            cardListViewModel
                        )
                    }
                    composable<NavDestination.AddDeck> { backStackEntry ->
                        AddDeckScreen(
                            onNavigateBack, addDeckViewModel = viewModel(
                                factory = AddDeckViewModelFactory(
                                    sharedDecksViewModel = getSharedDecksViewModel(
                                        app,
                                        backStackEntry,
                                        navController
                                    ),
                                    deckRepository = app.deckRepository
                                )
                            )
                        )
                    }
                    composable<NavDestination.EditDeck> { backStackEntry ->
                        val editDeckData: NavDestination.EditDeck = backStackEntry.toRoute()
                        val editDeckViewModel: EditDeckViewModel = viewModel(
                            factory = EditDeckViewModelFactory(
                                editDeckData = editDeckData,
                                sharedDecksViewModel = getSharedDecksViewModel(
                                    app,
                                    backStackEntry,
                                    navController
                                ),
                            )
                        )
                        EditDeckScreen(onNavigateBack, editDeckViewModel)
                    }
                    composable<NavDestination.AddCard> { backStackEntry ->
                        val addCardNavData: NavDestination.AddCard = backStackEntry.toRoute()
                        val sharedCardListViewModel: SharedCardListViewModel =
                            getSharedCardListViewModel(
                                flashcardRepository = app.flashcardRepository,
                                deckId = addCardNavData.deckId
                                    ?: "",     // WARN: might have to come back to this because deckId being "" might introduce unexpected behavior
                                backStackEntry,
                                navController
                            )

                        val sharedDecksViewModel = getSharedDecksViewModel(
                            app, backStackEntry, navController
                        )
                        val addCardViewModel: AddCardViewModel = viewModel(
                            factory = AddCardViewModelFactory(
                                addCardNavData,
                                flashcardRepository = app.flashcardRepository,
                                sharedDecksViewModel = sharedDecksViewModel,
                                sharedUiEventViewModel = sharedUiEventViewModel,
                                refetchCards = { sharedCardListViewModel.refetchCards() },
                            )
                        )
                        AddCardScreen(addCardViewModel, onNavigateBack, onNavigateToAddDeckScreen)
                    }
                    // End flashcard/deck management features


                    // Start user profile management
                    composable<NavDestination.Profile> { ProfileScreen(bottomNavBar) }
                    // End user profile management
                }
            }

            NavHost(navController = navController, graph = navGraph)
        }
    }

}