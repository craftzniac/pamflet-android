package com.example.pamflet

import kotlinx.serialization.Serializable

sealed class NavDestination{
    @Serializable
    data object Home: NavDestination()
}

