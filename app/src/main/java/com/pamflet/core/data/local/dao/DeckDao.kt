package com.pamflet.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.pamflet.core.data.local.entity.DeckEntity
import com.pamflet.core.data.local.entity.FlashcardEntity

data class DeckEntityWithCardCount(val id: String, val name: String, val cardCount: Int)
data class DeckEntityWithCards(val id: String, val name: String, val cards: List<FlashcardEntity>)

typealias UpdatedValuesCount = Int

@Dao
interface DeckDao {
    @Query(
        """
    SELECT deck_table.*, COUNT(flashcard_table.id) AS cardCount FROM deck_table
    LEFT JOIN flashcard_table ON deck_table.id = flashcard_table.deck_id
    GROUP BY deck_table.id
        """
    )
    suspend fun getAll(): List<DeckEntityWithCardCount>

    @Query("SELECT * FROM deck_table WHERE deck_table.id = :id")
    suspend fun getOne(id: String): DeckEntity?

    @Query("DELETE FROM deck_table WHERE deck_table.id = :deckId")
    suspend fun deleteOne(deckId: String)

    @Insert
    suspend fun createOne(deck: DeckEntity): Long

    @Update
    suspend fun updateOne(deck: DeckEntity): UpdatedValuesCount

    @Query("DELETE FROM deck_table")
    suspend fun deleteAll()
}
