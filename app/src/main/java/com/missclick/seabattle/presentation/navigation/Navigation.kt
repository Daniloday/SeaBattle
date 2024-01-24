package com.missclick.seabattle.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.missclick.seabattle.presentation.components.Battlefield
import com.missclick.seabattle.presentation.screens.battle.BattleRoute
import com.missclick.seabattle.presentation.screens.battle.BattleViewModel
import com.missclick.seabattle.presentation.screens.menu.MenuRoute
import com.missclick.seabattle.presentation.screens.multiplayer.MultiplayerRoute
import com.missclick.seabattle.presentation.screens.prepare.PrepareRoute


@Composable
fun Navigation(){

    val navController = rememberNavController()

    val battleViewModel : BattleViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = NavigationTree.Battle.route){

        composable(NavigationTree.Menu.route){
            MenuRoute(navController)
        }
        composable(NavigationTree.Multiplayer.route){
            MultiplayerRoute(navController)
        }


        composable(NavigationTree.Battle.route){
            BattleRoute(navController, battleViewModel)
        }

        composable(NavigationTree.Prepare.route){
            PrepareRoute(navController)
        }

    }


}