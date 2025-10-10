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
            val navGraph = remember(navController) {
                navController.createGraph(startDestination = NavDestination.Decks) {
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
                    composable<NavDestination.CardsSlide> { backStackEntry ->
                        val cardsSlide: NavDestination.CardsSlide = backStackEntry.toRoute()
                        CardsSlideScreen(cardsSlide)
                    }
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