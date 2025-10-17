package com.example.pamflet.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.pamflet.components.SimpleTopAppBar

@Composable
fun ManageDecksScreen(bottomNavBar: @Composable () -> Unit) {
    Scaffold(
        topBar = { SimpleTopAppBar(title = "Manage Decks") },
        bottomBar = bottomNavBar
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {

        }
    }
}