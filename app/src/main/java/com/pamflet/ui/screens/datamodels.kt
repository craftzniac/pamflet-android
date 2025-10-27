package com.pamflet.ui.screens

data class Flashcard(
    val id: String,
    var front: String,
    var back: String,
)

data class DeckWithCards(
    val id: String,
    val name: String,
    val cards: List<Flashcard> = listOf()
)

data class Deck(
    val id: String,
    val name: String,
    val cardCount: Int,
)
