package com.pamflet.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.createGraph
import androidx.navigation.toRoute
import com.pamflet.PamfletApplication
import com.pamflet.core.domain.GetAggregatedFlashcardsUseCase
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
import com.pamflet.features.review.ui.ReviewViewModel
import com.pamflet.features.review.ui.ReviewViewModelFactory
import com.pamflet.features.review.ui.SetupReviewScreen
import com.pamflet.features.review.ui.SetupReviewViewModelFactory
import com.pamflet.shared.ui.components.BottomNavBar
import com.pamflet.shared.viewmodel.SharedCardListViewModel
import com.pamflet.shared.viewmodel.SharedDecksViewModel
import com.pamflet.shared.viewmodel.SharedUiEventViewModel
import com.pamflet.shared.viewmodel.getSharedCardListViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    app: PamfletApplication,
    sharedUiEventViewModel: SharedUiEventViewModel,
    sharedDecksViewModel: SharedDecksViewModel
) {
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

            val onNavigateToReviewScreen = { data: NavDestination.Review ->
                navController.navigate(
                    route = NavDestination.Review(
                        selectedDeckIds = data.selectedDeckIds,
                        maxNumberOfCards = data.maxNumberOfCards,
                        isShuffleCards = data.isShuffleCards
                    )
                )
            }

            val onNavigateToEditCardScreen = { data: NavDestination.EditCard ->
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
                            sharedDecksViewModel,
                            deckRepository = app.deckRepository
                        )
                    ), bottomNavBar, onNavigateToReviewScreen
                )
            }
            composable<NavDestination.Review> { backStackEntry ->
                val reviewNavData: NavDestination.Review = backStackEntry.toRoute()
                val reviewViewModel: ReviewViewModel = viewModel(
                    factory = ReviewViewModelFactory(
                        reviewNavData,
                        GetAggregatedFlashcardsUseCase(app.flashcardRepository)
                    )
                )
                ReviewScreen(
                    reviewViewModel, onNavigateBack, onNavigateToEditCardScreen
                )
            }
            // End flashcard usage features


            // Start flashcard/deck management features
            composable<NavDestination.ManageDecks> { backStackEntry ->
                ManageDecksScreen(
                    bottomNavBar,
                    manageDecksViewModel = viewModel(
                        factory = ManageDecksViewModelFactory(sharedDecksViewModel)
                    ),
                    onNavigateToDeckCardsListScreen,
                    onNavigateToAddDeckScreen,
                    onNavigateToEditDeckScreen
                )
            }
            composable<NavDestination.EditCard> { backStackEntry ->

                val editCardNavData: NavDestination.EditCard = backStackEntry.toRoute()

                val sharedCardListViewModel: SharedCardListViewModel =
                    getSharedCardListViewModel(
                        flashcardRepository = app.flashcardRepository,
                        deckId = editCardNavData.deckId,
                        backStackEntry,
                        navController
                    )

                val editCardViewModel: EditCardViewModel =
                    viewModel(
                        factory = EditCardViewModelFactory(
                            editCardNavData,
                            flashcardRepository = app.flashcardRepository,
                            sharedDecksViewModel,
                            sharedUiEventViewModel,
                            refetchCards = { sharedCardListViewModel.refetchCards() })
                    )
                EditCardScreen(editCardViewModel, onNavigateBack, onNavigateToAddDeckScreen)
            }
            composable<NavDestination.CardList> { backStackEntry ->
                val cardListNavData: NavDestination.CardList = backStackEntry.toRoute()

                val cardListViewModel: CardListViewModel =
                    viewModel(
                        factory = DeckCardListViewModelFactory(
                            cardListNavData = cardListNavData,
                            flashcardRepository = app.flashcardRepository,
                            deckRepository = app.deckRepository,
                            sharedCardListViewModel = getSharedCardListViewModel(
                                flashcardRepository = app.flashcardRepository,
                                deckId = cardListNavData.selectedDeckId,
                                backStackEntry = backStackEntry,
                                navController = navController
                            ),
                            sharedUiEventViewModel,
                            sharedDecksViewModel
                        ),
                    )

                CardListScreen(
                    onNavigateBack,
                    onNavigateToAddCardScreen,
                    onNavigateToEditCardScreen,
                    cardListViewModel,
                    onNavigateToEditDeckScreen
                )
            }
            composable<NavDestination.AddDeck> { backStackEntry ->
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
                val editDeckData: NavDestination.EditDeck = backStackEntry.toRoute()
                val editDeckViewModel: EditDeckViewModel = viewModel(
                    factory = EditDeckViewModelFactory(
                        editDeckData = editDeckData,
                        sharedDecksViewModel = sharedDecksViewModel,
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