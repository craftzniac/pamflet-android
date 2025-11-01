package com.pamflet

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.pamflet.core.data.local.PamfletDatabase
import com.pamflet.core.data.repository.DeckRepository
import com.pamflet.core.data.repository.FlashcardRepository

class PamfletApplication : Application() {
    val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            PamfletDatabase::class.java,
            "pamflet.db"
        ).fallbackToDestructiveMigration(false).build()
    }

    val flashcardRepository by lazy {
        FlashcardRepository(flashcardDao = database.flashcardDao)
    }

    val deckRepository by lazy {
        DeckRepository(
            deckDao = database.deckDao,
        )
    }

    override fun onCreate() {
        super.onCreate()
        // verify that database connects successfully
        Log.d("PamfletApplication", "Database connected: ${database.isOpen}")
    }

}