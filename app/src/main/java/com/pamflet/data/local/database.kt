package com.pamflet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pamflet.data.local.dao.DeckDao
import com.pamflet.data.local.dao.FlashcardDao
import com.pamflet.data.local.entity.DeckEntity
import com.pamflet.data.local.entity.FlashcardEntity

@Database(
    entities = [DeckEntity::class, FlashcardEntity::class], version = 2
)
abstract class PamfletDatabase : RoomDatabase() {
    abstract val deckDao: DeckDao
    abstract val flashcardDao: FlashcardDao
}