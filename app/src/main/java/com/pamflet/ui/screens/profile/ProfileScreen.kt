package com.pamflet.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pamflet.ui.components.SimpleTopAppBar

@Composable
fun ProfileScreen(bottomNavBar: @Composable () -> Unit) {
    Scaffold(
        topBar = { SimpleTopAppBar(title = "Profile") },
        bottomBar = bottomNavBar
    ) { contentPadding ->
        Box(
            modifier = Modifier.padding(contentPadding)
        ) {
            Column {
                Text("Profile screen")
            }
        }

    }
}