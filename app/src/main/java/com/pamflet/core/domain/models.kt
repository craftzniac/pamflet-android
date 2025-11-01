package com.pamflet.core.domain

import com.pamflet.core.data.local.entity.DeckEntity
import com.pamflet.core.data.local.entity.FlashcardEntity

data class Flashcard(
    val id: String,
    var front: String,
    var back: String,
) {
    companion object {
        fun fromFlashcardEntity(card: FlashcardEntity): Flashcard {
            return Flashcard(id = card.id, front = card.front, back = card.back)
        }
    }
}

data class DeckWithCards(
    val id: String, val name: String, val cards: List<Flashcard> = listOf()
) {
    fun toDeck(): Deck {
        return Deck(id = this.id, name = this.name, cardCount = -1)
    }
}

data class Deck(
    val id: String,
    val name: String,
    val cardCount: Int,
) {
    fun toDeckEntity(): DeckEntity {
        return DeckEntity(id = this.id, name = this.name)
    }

    companion object {
        fun fromDeckEntity(deckEntity: DeckEntity): Deck {
            return Deck(id = deckEntity.id, name = deckEntity.name, cardCount = -1)
        }
    }
}