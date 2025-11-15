package com.pamflet.navigation

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
    data class Review(
        val selectedDeckIds: List<String>,
        val maxNumberOfCards: Int,
        val isShuffleCards: Boolean
    ) : NavDestination() {
        override val serialName: String
            get() = serializer().descriptor.serialName
    }

    @Serializable
    data object SetupReview : NavDestination() {
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
    data object Settings : NavDestination() {
        override val serialName: String
            get() = serializer().descriptor.serialName
    }

    @Serializable
    data class EditCard(
        val selectedCardId: String,
        val deckId: String
    ) : NavDestination() {
        override val serialName: String
            get() = serializer().descriptor.serialName
    }

    @Serializable
    data class AddCard(val deckId: String?) : NavDestination() {
        override val serialName: String
            get() = serializer().descriptor.serialName
    }

    @Serializable
    data class CardList(val selectedDeckId: String) : NavDestination() {
        override val serialName: String
            get() = serializer().descriptor.serialName
    }

    @Serializable
    data object AddDeck : NavDestination() {
        override val serialName: String
            get() = serializer().descriptor.serialName
    }

    @Serializable
    data class EditDeck(val deckId: String) : NavDestination() {
        override val serialName: String
            get() = serializer().descriptor.serialName
    }
}

