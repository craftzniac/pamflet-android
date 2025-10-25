package com.pamflet.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import com.pamflet.data.local.entity.FlashcardEntity

@Dao
interface FlashcardDao {
    @Query("SELECT * FROM flashcard_table WHERE flashcard_table.id = :id AND flashcard_table.deck_id = :deckId")
    fun getOne(id: String, deckId: String): FlashcardEntity?

//    @Upsert
//    fun upsertOne(flashcard: Flashcard)

    @Query("SELECT * FROM flashcard_table WHERE flashcard_table.deck_id = :deckId")
    fun getAll(deckId: String): List<FlashcardEntity>

    @Delete
    fun delete(flashcard: FlashcardEntity)
}

