package com.pamflet.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcard_table")
data class FlashcardEntity(
    @PrimaryKey val id: String,
    val front: String,
    val back: String,
    @ColumnInfo(name = "deck_id") val deckId: String
)

