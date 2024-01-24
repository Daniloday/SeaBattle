package com.missclick.seabattle.presentation.navigation

sealed class NavigationTree(val route : String) {

    data object Menu : NavigationTree("menu")
    data object Multiplayer : NavigationTree("multiplayer")
    data object Battle : NavigationTree("battle")


}