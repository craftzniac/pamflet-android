package com.example.pamflet.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomNavBar(
    onNavigateToCardsSlideSetupScreen: () -> Unit,
    onNavigateToManageDecksScreen: () -> Unit,
    onNavigateToProfileScreen: () -> Unit,
) {
    NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
        NavigationBarItem(
            selected = false,
            onClick = { onNavigateToCardsSlideSetupScreen() },
            icon = {},
            label = {
                Text("Cards Slide")
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = { onNavigateToManageDecksScreen() },
            icon = {},
            label = {
                Text("Manage Decks")
            }
        )
        NavigationBarItem(
            selected = false,
            onClick = { onNavigateToProfileScreen() },
            icon = {},
            label = {
                Text("Profile")
            }
        )
    }
}