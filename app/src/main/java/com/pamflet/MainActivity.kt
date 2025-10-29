package com.pamflet

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.pamflet.ui.screens.cardsslide.CardsSlideScreen
import com.pamflet.ui.components.BottomNavBar
import com.pamflet.ui.screens.DecksSharedViewModel
import com.pamflet.ui.screens.DecksSharedViewModelFactory
import com.pamflet.ui.screens.cardsslidesetup.CardsSlideSetupScreen
import com.pamflet.ui.screens.managedecks.deckcardslist.DeckCardsListScreen
import com.pamflet.ui.screens.managedecks.deckcardsslideedit.DeckCardsSlideEditScreen
import com.pamflet.ui.screens.auth.login.LoginScreen
import com.pamflet.ui.screens.managedecks.ManageDecksScreen
import com.pamflet.ui.screens.profile.ProfileScreen
import com.pamflet.ui.screens.auth.signup.SignupScreen
import com.pamflet.ui.screens.cardsslidesetup.CardsSlideSetupViewModelFactory
import com.pamflet.ui.screens.managedecks.ManageDecksViewModelFactory
import com.pamflet.ui.screens.managedecks.addoreditdeck.AddDeckScreen
import com.pamflet.ui.screens.managedecks.addoreditdeck.AddDeckViewModelFactory
import com.pamflet.ui.theme.PamfletTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = (application as PamfletApplication)
        setContent {
            App(app)
        }
    }
}

@Composable
fun App(app: PamfletApplication) {
    PamfletTheme(
        darkTheme = false
    ) {
        val snackBarHostState = remember { SnackbarHostState() }
        Scaffold(snackbarHost = { SnackbarHost(snackBarHostState) }, topBar = {}) { paddingValues ->
            Log.d("Composable::App", "paddingValue: $paddingValues")
            Box(
                modifier = Modifier.fillMaxSize()
//                    .padding(paddingValues)
            ) {
                val navController = rememberNavController()

                val currentSelectedRoute =
                    navController.currentBackStackEntryAsState().value?.destination?.route
                val onNavigateToProfileScreen = {
                    navController.navigate(route = NavDestination.Profile)
                }

                val onNavigateToCardsSlideSetupScreen = {
                    navController.navigate(route = NavDestination.CardsSlideSetup)
                }

                val onNavigateToManageDecksScreen = {
                    navController.navigate(route = NavDestination.ManageDecks)
                }

                val onNavigateToCardsSlideScreen = { data: NavDestination.CardsSlide ->
                    navController.navigate(
                        route = NavDestination.CardsSlide(
                            selectedDeckIds = data.selectedDeckIds,
                            maxNumberOfCards = data.maxNumberOfCards,
                            isShuffleCards = data.isShuffleCards
                        )
                    )
                }

                val onNavigateToDeckCardsSlideEditScreen =
                    { data: NavDestination.DeckCardsSlideEdit ->
                        navController.navigate(
                            route = NavDestination.DeckCardsSlideEdit(
                                selectedCardId = data.selectedCardId
                            )
                        )
                    }

                val onNavigateToDeckCardsListScreen = { data: NavDestination.DeckCardsList ->
                    navController.navigate(route = data)
                }

                val onNavigateToAddDeckScreen =
                    { navController.navigate(route = NavDestination.AddDeck) }

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
                        // Start Auth screens
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
                        // End Auth screens


                        // Start flashcard usage screens
                        composable<NavDestination.CardsSlideSetup> { backStackEntry ->
                            val decksSharedViewModel: DecksSharedViewModel = viewModel(
                                factory = DecksSharedViewModelFactory(
                                    deckRepository = app.deckRepository,
                                    flashcardRepository = app.flashcardRepository
                                ),
                                viewModelStoreOwner = remember(backStackEntry) {
                                    navController.getBackStackEntry(route = NavDestination.Root)
                                })
                            CardsSlideSetupScreen(
                                cardsSlideSetupViewModel = viewModel(
                                    factory = CardsSlideSetupViewModelFactory(
                                        decksSharedViewModel, deckRepository = app.deckRepository
                                    )
                                ), bottomNavBar, onNavigateToCardsSlideScreen
                            )
                        }
                        composable<NavDestination.CardsSlide> { backStackEntry ->
                            val cardsSlide: NavDestination.CardsSlide = backStackEntry.toRoute()
                            CardsSlideScreen(
                                cardsSlide, onNavigateBack, onNavigateToDeckCardsSlideEditScreen
                            )
                        }
                        // End flashcard usage screens


                        // Start flashcard/deck management screens
                        composable<NavDestination.ManageDecks> { backStackEntry ->
                            val decksSharedViewModel: DecksSharedViewModel = viewModel(
                                factory = DecksSharedViewModelFactory(
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
                                    factory = ManageDecksViewModelFactory(
                                        decksSharedViewModel, deckRepository = app.deckRepository
                                    )
                                ), onNavigateToDeckCardsListScreen, onNavigateToAddDeckScreen
                            )
                        }
                        composable<NavDestination.DeckCardsSlideEdit> { backStackEntry ->
                            val deckCardsSlideEdit: NavDestination.DeckCardsSlideEdit =
                                backStackEntry.toRoute()
                            DeckCardsSlideEditScreen(deckCardsSlideEdit, onNavigateBack)
                        }
                        composable<NavDestination.DeckCardsList> { backStackEntry ->
                            val deckCardsList: NavDestination.DeckCardsList =
                                backStackEntry.toRoute()
                            DeckCardsListScreen(
                                deckCardsList, onNavigateBack, onNavigateToDeckCardsSlideEditScreen
                            )
                        }
                        composable<NavDestination.AddDeck> {
                            val decksSharedViewModel: DecksSharedViewModel = viewModel(
                                viewModelStoreOwner = navController.getBackStackEntry(NavDestination.Root),
                                factory = DecksSharedViewModelFactory(
                                    app.deckRepository,
                                    app.flashcardRepository
                                )
                            )

                            AddDeckScreen(
                                onNavigateBack, addDeckViewModel = viewModel(
                                    factory = AddDeckViewModelFactory(
                                        decksSharedViewModel = decksSharedViewModel,
                                        deckRepository = app.deckRepository
                                    )
                                )
                            )
                        }
                        // End flashcard/deck management screens


                        // Start user profile management
                        composable<NavDestination.Profile> { ProfileScreen(bottomNavBar) }
                        // End user profile management
                    }
                }
                NavHost(navController = navController, graph = navGraph)
            }
        }
    }
}