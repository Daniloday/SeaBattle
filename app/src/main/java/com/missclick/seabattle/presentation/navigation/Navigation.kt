package com.missclick.seabattle.presentation.navigation

import android.webkit.WebView
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
import com.missclick.seabattle.presentation.screens.settings.SettingsRoute
import com.missclick.seabattle.presentation.screens.single.SingleRoute
import com.missclick.seabattle.presentation.screens.waiting.WaitingRoute


@Composable
fun Navigation(){

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavigationTree.Menu.route){

        composable(NavigationTree.Menu.route){
            MenuRoute(navController)
        }

        composable(NavigationTree.Multiplayer.route){
            MultiplayerRoute(navController)
        }


        composable(NavigationTree.Battle.route + "/{${NavigationKeys.CODE}}/{${NavigationKeys.IS_OWNER}}"){
            BattleRoute(navController)
        }

        composable(NavigationTree.Prepare.route + "/{${NavigationKeys.CODE}}/{${NavigationKeys.IS_OWNER}}"){
            PrepareRoute(navController)
        }

        composable(NavigationTree.Waiting.route){
            WaitingRoute(navController)
        }

        composable(NavigationTree.Settings.route){
            SettingsRoute(navController = navController)
        }

        composable(NavigationTree.Single.route){
            SingleRoute(navController = navController)
        }

    }


}