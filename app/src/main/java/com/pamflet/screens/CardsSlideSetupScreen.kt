package com.pamflet.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.pamflet.components.SimpleTopAppBar

@Composable
fun CardsSlideSetupScreen(bottomNavBar: @Composable () -> Unit) {
    Scaffold(
        topBar = { SimpleTopAppBar(title = "Card Slide Setup", isShowPamfletLogo = true) },
        bottomBar = bottomNavBar
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(paddingValues)
        ) {
            Column {
                Text("Cards slide setup screen")
            }
        }
    }
}