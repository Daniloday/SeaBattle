package com.missclick.seabattle.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.missclick.seabattle.presentation.screens.menu.MenuRoute

@Composable
fun Navigation(){

    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = NavigationTree.Menu.route){
        
        composable(NavigationTree.Menu.route){
            MenuRoute()
        }
        
    }


}