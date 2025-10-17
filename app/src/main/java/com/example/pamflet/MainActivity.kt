package com.example.pamflet

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
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.toRoute
import com.example.pamflet.components.BottomNavBar
import com.example.pamflet.screens.CardsSlideSetupScreen
import com.example.pamflet.screens.LoginScreen
import com.example.pamflet.screens.ManageDecksScreen
import com.example.pamflet.screens.ProfileScreen
import com.example.pamflet.screens.SignupScreen
import com.example.pamflet.ui.theme.PamfletTheme

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

            val onNavigateToProfileScreen = {
                navController.navigate(route = NavDestination.Profile)
            }

            val onNavigateToCardsSlideSetupScreen = {
                navController.navigate(route = NavDestination.CardsSlideSetup)
            }

            val onNavigateToManageDecksScreen = {
                navController.navigate(route = NavDestination.ManageDecks)
            }

            val bottomNavBar = @Composable {
                BottomNavBar(
                    onNavigateToCardsSlideSetupScreen,
                    onNavigateToManageDecksScreen,
                    onNavigateToProfileScreen
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
                            bottomNavBar
                        )
                    }
                    composable<NavDestination.CardsSlide> { backStackEntry ->
                        val cardsSlide: NavDestination.CardsSlide = backStackEntry.toRoute()
                        CardsSlideScreen(cardsSlide, onNavigateBack = {
                            navController.popBackStack()
                        })
                    }
                    // End flashcard usage screens


                    // Start flashcard/deck management screens
                    composable<NavDestination.ManageDecks> { ManageDecksScreen(bottomNavBar) }
                    composable<NavDestination.Decks> {
                        DecksScreen(
                            data = NavDestination.Decks,
                            onNavigateToEditorPreviewTest = {
                                navController.navigate(route = NavDestination.EditorPreviewTest)
                            },
                            onNavigateToCardsSlideScreen = { deckIds ->
                                navController.navigate(route = NavDestination.CardsSlide(deckIds))
                            }
                        )
                    }
                    composable<NavDestination.EditorPreviewTest> { EditorPreviewTestScreen() }
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