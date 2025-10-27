package com.pamflet.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "flashcard_table")
data class FlashcardEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val front: String,
    val back: String,
    @ColumnInfo(name = "deck_id") val deckId: String
)

