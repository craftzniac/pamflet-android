package com.pamflet.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.pamflet.NavDestination
import com.pamflet.ui.theme.Purple500

fun isRouteSelected(route: NavDestination, currentSelectedRoute: String?): Boolean {
    return route.serialName == currentSelectedRoute
}

data class NavBarItem(
    val route: NavDestination,
    val icon: @Composable () -> Unit,
    val label: String,
    val onClick: () -> Unit,

    )

@Composable
fun BottomNavBar(
    onNavigateToCardsSlideSetupScreen: () -> Unit,
    onNavigateToManageDecksScreen: () -> Unit,
    onNavigateToProfileScreen: () -> Unit,
    currentSelectedRoute: String?
) {
    val navbarItems = listOf(
        NavBarItem(
            route = NavDestination.CardsSlideSetup,
            icon = @Composable {
                Box(modifier = Modifier.size(24.dp)) { Logo() }
            },
            label = "Cards Slide",
            onClick = onNavigateToCardsSlideSetupScreen
        ),
        NavBarItem(
            route = NavDestination.ManageDecks,
            icon = @Composable {
                Icon(
                    painter = painterResource(id = com.pamflet.R.drawable.nav_scroll),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = "Manage Decks",
            onClick = onNavigateToManageDecksScreen
        ),
        NavBarItem(
            route = NavDestination.Profile,
            icon = @Composable {
                Icon(
                    painter = painterResource(id = com.pamflet.R.drawable.nav_profile),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp)
                )
            },
            label = "Profile",
            onClick = onNavigateToProfileScreen
        ),
    )

    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets,
        containerColor = Color.White,
        contentColor = Color.Unspecified
    ) {
        navbarItems.forEach { item ->
            NavigationBarItem(
                selected = isRouteSelected(item.route, currentSelectedRoute),
                onClick = item.onClick,
                icon = item.icon,
                label = {
                    Text(text = item.label)
                },
                colors = NavigationBarItemDefaults.colors(selectedTextColor = Purple500)
            )
        }
    }
}