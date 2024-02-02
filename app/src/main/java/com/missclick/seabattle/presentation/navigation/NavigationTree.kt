package com.missclick.seabattle.presentation.navigation

sealed class NavigationTree(val route : String) {

    data object Menu : NavigationTree("menu")
    data object Multiplayer : NavigationTree("multiplayer")
    data object Prepare: NavigationTree("prepare")
    data object Battle : NavigationTree("battle")
    data object Waiting : NavigationTree("waiting")
    data object Settings : NavigationTree("settings")


}