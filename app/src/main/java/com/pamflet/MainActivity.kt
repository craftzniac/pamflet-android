package com.pamflet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.toRoute
import com.pamflet.screens.CardsSlideScreen
import com.pamflet.components.BottomNavBar
import com.pamflet.screens.CardsSlideSetupScreen
import com.pamflet.screens.DeckCardsListScreen
import com.pamflet.screens.DeckCardsSlideEditScreen
import com.pamflet.screens.LoginScreen
import com.pamflet.screens.ManageDecksScreen
import com.pamflet.screens.ProfileScreen
import com.pamflet.screens.SignupScreen
import com.pamflet.ui.theme.PamfletTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}


@Composable
fun App() {
    PamfletTheme(
        darkTheme = false
    ) {
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

            val onNavigateToDeckCardsSlideEditScreen = { data: NavDestination.DeckCardsSlideEdit ->
                navController.navigate(
                    route = NavDestination.DeckCardsSlideEdit(
                        selectedCardId = data.selectedCardId
                    )
                )
            }

            val onNavigateToDeckCardsListScreen = { data: NavDestination.DeckCardsList ->
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
                navController.createGraph(startDestination = NavDestination.Login) {
                    // Start Auth screens
                    composable<NavDestination.Login> {
                        LoginScreen(
                            onNavigateToSignupScreen = {
                                navController.navigate(route = NavDestination.Signup)
                            },
                            onNavigateToCardsSlideSetupScreen
                        )
                    }
                    composable<NavDestination.Signup> {
                        SignupScreen(
                            onNavigateToLoginScreen = {
                                navController.navigate(route = NavDestination.Login)
                            },
                            onNavigateToManageDecksScreen
                        )
                    }
                    // End Auth screens


                    // Start flashcard usage screens
                    composable<NavDestination.CardsSlideSetup> {
                        CardsSlideSetupScreen(
                            bottomNavBar,
                            onNavigateToCardsSlideScreen
                        )
                    }
                    composable<NavDestination.CardsSlide> { backStackEntry ->
                        val cardsSlide: NavDestination.CardsSlide = backStackEntry.toRoute()
                        CardsSlideScreen(
                            cardsSlide,
                            onNavigateBack,
                            onNavigateToDeckCardsSlideEditScreen
                        )
                    }
                    // End flashcard usage screens


                    // Start flashcard/deck management screens
                    composable<NavDestination.ManageDecks> {
                        ManageDecksScreen(bottomNavBar, onNavigateToDeckCardsListScreen)
                    }
//                    composable<NavDestination.Decks> {
//                        DecksScreen(
//                            data = NavDestination.Decks,
//                            onNavigateToEditorPreviewTest = {
//                                navController.navigate(route = NavDestination.EditorPreviewTest)
//                            },
//                            onNavigateToCardsSlideScreen
//                        )
//                    }
                    composable<NavDestination.EditorPreviewTest> { EditorPreviewTestScreen() }
                    composable<NavDestination.DeckCardsSlideEdit> { backStackEntry ->
                        val deckCardsSlideEdit: NavDestination.DeckCardsSlideEdit =
                            backStackEntry.toRoute()
                        DeckCardsSlideEditScreen(deckCardsSlideEdit, onNavigateBack)
                    }
                    composable<NavDestination.DeckCardsList> { backStackEntry ->
                        val deckCardsList: NavDestination.DeckCardsList = backStackEntry.toRoute()
                        DeckCardsListScreen(
                            deckCardsList,
                            onNavigateBack,
                            onNavigateToDeckCardsSlideEditScreen
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


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PamfletPreview() {
    App()
}