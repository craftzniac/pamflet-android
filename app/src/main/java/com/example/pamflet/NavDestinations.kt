package com.example.pamflet

import kotlinx.serialization.Serializable

sealed class NavDestination{
    @Serializable
    data object EditorPreviewTest: NavDestination()

    @Serializable
    data object Decks: NavDestination()

    @Serializable
    data class CardsSlide(
        val deckIds: List<String>
    ): NavDestination()
}

