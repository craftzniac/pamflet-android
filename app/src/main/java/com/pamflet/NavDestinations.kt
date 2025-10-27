package com.pamflet

import kotlinx.serialization.Serializable

interface Routable {
    val serialName: String
}

@Serializable
sealed class NavDestination : Routable {
    @Serializable
    data object Root

    @Serializable
    data object EditorPreviewTest : NavDestination() {
        override val serialName: String
            get() = serializer().descriptor.serialName
    }

    @Serializable
    data object Decks : NavDestination() {
        override val serialName: String
            get() = serializer().descriptor.serialName
    }

    @Serializable
    data class CardsSlide(
        val selectedDeckIds: List<String>,
        val maxNumberOfCards: Int,
        val isShuffleCards: Boolean
    ) : NavDestination() {
        override val serialName: String
            get() = serializer().descriptor.serialName
    }

    @Serializable
    data object CardsSlideSetup : NavDestination() {
        override val serialName: String
            get() = serializer().descriptor.serialName
    }

    @Serializable
    data object Login : NavDestination() {
        override val serialName: String
            get() = serializer().descriptor.serialName
    }

    @Serializable
    data object Signup : NavDestination() {
        override val serialName: String
            get() = serializer().descriptor.serialName
    }

    @Serializable
    data object ManageDecks : NavDestination() {
        override val serialName: String
            get() = serializer().descriptor.serialName
    }

    @Serializable
    data object Profile : NavDestination() {
        override val serialName: String
            get() = serializer().descriptor.serialName
    }

    @Serializable
    data class DeckCardsSlideEdit(
        val selectedCardId: String?
    ) : NavDestination() {
        override val serialName: String
            get() = serializer().descriptor.serialName
    }

    @Serializable
    data class DeckCardsList(val selectedDeckId: String) : NavDestination() {
        override val serialName: String
            get() = serializer().descriptor.serialName
    }

    @Serializable
    data object AddDeck
}

