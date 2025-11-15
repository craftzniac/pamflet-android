package com.pamflet.shared.ui.components

import android.util.Log
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
import com.pamflet.navigation.NavDestination
import com.pamflet.R
import com.pamflet.shared.ui.theme.Gray900
import com.pamflet.shared.ui.theme.Purple500

fun isRouteSelected(route: NavDestination, currentSelectedRoute: String?): Boolean {
    Log.d("isRouteSelected", "currentRoute: $currentSelectedRoute,  route: ${route.serialName}")
    return route.serialName == currentSelectedRoute
}

data class NavBarItem(
    val route: NavDestination,
    val icon: @Composable (isSelected: Boolean) -> Unit,
    val label: String,
    val onClick: () -> Unit
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
            route = NavDestination.SetupReview, icon = @Composable {
                Box(modifier = Modifier.size(24.dp)) { Logo() }
            }, label = "Cards Review", onClick = onNavigateToCardsSlideSetupScreen
        ),
        NavBarItem(
            route = NavDestination.ManageDecks, icon = @Composable { isSelected ->
                Icon(
                    painter = painterResource(id = R.drawable.nav_scroll),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                    tint = if (isSelected) Purple500 else Gray900
                )
            }, label = "Manage Decks", onClick = onNavigateToManageDecksScreen
        ),
        NavBarItem(
            route = NavDestination.Profile, icon = @Composable { isSelected ->
                Icon(
                    painter = painterResource(id = R.drawable.nav_profile),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                    tint = if (isSelected) Purple500 else Gray900
                )
            }, label = "Profile", onClick = onNavigateToProfileScreen
        ),
    )

    NavigationBar(
        windowInsets = NavigationBarDefaults.windowInsets,
        containerColor = Color.White,
        contentColor = Color.Unspecified
    ) {
        navbarItems.forEach { item ->
            val isRouteSelected = isRouteSelected(item.route, currentSelectedRoute)
            NavigationBarItem(
                selected = isRouteSelected,
                onClick = item.onClick,
                icon = { item.icon(isRouteSelected) },
                label = {
                    Text(text = item.label)
                },
                colors = NavigationBarItemDefaults.colors(selectedTextColor = Purple500)
            )
        }
    }
}