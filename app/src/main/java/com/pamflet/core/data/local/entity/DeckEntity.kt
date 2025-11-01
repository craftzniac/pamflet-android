package com.pamflet.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "deck_table")
data class DeckEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
)

