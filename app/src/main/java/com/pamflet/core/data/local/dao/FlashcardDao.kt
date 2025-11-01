package com.pamflet.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.pamflet.core.data.local.entity.FlashcardEntity

@Dao
interface FlashcardDao {
    @Query("SELECT * FROM flashcard_table WHERE flashcard_table.id = :id AND flashcard_table.deck_id = :deckId")
    suspend fun getOne(id: String, deckId: String): FlashcardEntity?

    @Query("SELECT * FROM flashcard_table WHERE flashcard_table.deck_id = :deckId")
    suspend fun getAll(deckId: String): List<FlashcardEntity>

    @Delete
    suspend fun delete(flashcard: FlashcardEntity)

    @Query("DELETE FROM flashcard_table WHERE flashcard_table.deck_id = :deckId")
    suspend fun deleteAllFromDeck(deckId: String)
}

