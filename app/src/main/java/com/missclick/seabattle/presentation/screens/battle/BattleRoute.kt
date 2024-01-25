package com.missclick.seabattle.presentation.screens.battle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.missclick.seabattle.presentation.components.BackMark
import com.missclick.seabattle.presentation.components.Battlefield
import com.missclick.seabattle.presentation.components.listBattlefield

@Composable
fun BattleRoute(navController: NavHostController, battleViewModel: BattleViewModel) {


    val uiState by battleViewModel.uiState.collectAsState()

    when(val uiStateCurrent = uiState){
        is BattleUiState.Loading -> {
            CircularProgressIndicator()}
        is BattleUiState.Error -> {
            Text(text = uiStateCurrent.errorName, color = Color.Red, fontSize = 10.sp)
        }
        is BattleUiState.Success -> {
            Text(text = uiStateCurrent.toString(), color = Color.Green, fontSize = 10.sp)
            Battlefield(listBattlefield = uiStateCurrent.friendCells, modifier = Modifier)
        }


        else -> {}
    }

    //TODO adaptive battle screen
    //todo add arrow of whose turn
//    Box(modifier = Modifier.fillMaxSize()) {
//        val orientation = LocalConfiguration.current.orientation
//
//        val battleFieldSize = listOf(LocalConfiguration.current.screenWidthDp, LocalConfiguration.current.screenHeightDp).min() - 90
//
//        when (orientation) {
//            1 -> {
//                //portrait
//                Column(
//                    Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.SpaceAround,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Battlefield(
//                        listBattlefield = listBattlefield, modifier = Modifier
//                            .size(battleFieldSize.dp)
//                    )
//                    Battlefield(
//                        listBattlefield = listBattlefield, modifier = Modifier
//                            .size(battleFieldSize.dp)
//                    )
//                }
//            }
//
//            2 -> {
//                //landscape
//                Row(
//                    Modifier.fillMaxSize(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceAround
//                ) {
//                    Battlefield(
//                        listBattlefield = listBattlefield, modifier = Modifier
//                            .size(battleFieldSize.dp)
//                    )
//                    Battlefield(
//                        listBattlefield = listBattlefield, modifier = Modifier
//                            .size(battleFieldSize.dp)
//                    )
//                }
//
//            }
//        }
//
//
//
//        BackMark {
//
//        }
//    }


}