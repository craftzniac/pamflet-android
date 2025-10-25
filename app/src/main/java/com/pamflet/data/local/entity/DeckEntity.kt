package com.pamflet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deck_table")
data class DeckEntity(
    @PrimaryKey val id: String,
    val name: String,
)

