package com.pamflet.data.repository

import com.pamflet.data.local.dao.DeckDao

class DeckRepository(
    private val deckDao: DeckDao,
    private val flashcardRepository: FlashcardRepository
) {
    suspend fun getDecks() = deckDao.getAll()
}