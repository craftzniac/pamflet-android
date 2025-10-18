package com.pamflet

import kotlinx.serialization.Serializable

sealed class NavDestination {
    @Serializable
    data object EditorPreviewTest : NavDestination()

    @Serializable
    data object Decks : NavDestination()

    @Serializable
    data class CardsSlide(
        val deckIds: List<String>
    ) : NavDestination()

    @Serializable
    data object CardsSlideSetup : NavDestination()

    @Serializable
    data object Login : NavDestination()

    @Serializable
    data object Signup : NavDestination()

    @Serializable
    data object ManageDecks: NavDestination()

    @Serializable
    data object Profile: NavDestination()
}

