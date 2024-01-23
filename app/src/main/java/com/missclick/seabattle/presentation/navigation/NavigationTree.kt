package com.missclick.seabattle.presentation.navigation

sealed class NavigationTree(val route : String) {

    data object Menu : NavigationTree("menu")

}