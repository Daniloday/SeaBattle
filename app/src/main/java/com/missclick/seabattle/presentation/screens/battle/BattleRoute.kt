package com.missclick.seabattle.presentation.screens.battle

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.missclick.seabattle.presentation.components.BackMark
import com.missclick.seabattle.presentation.components.Battlefield
import com.missclick.seabattle.presentation.components.Connecting
import com.missclick.seabattle.presentation.components.listBattlefield

@Composable
fun BattleRoute(navController: NavHostController, battleViewModel: BattleViewModel = hiltViewModel()) {


    val uiState by battleViewModel.uiState.collectAsState()

    BattleScreen(uiState = uiState, obtainEvent = battleViewModel::obtainEvent )


}

@Composable
fun BattleScreen(uiState : BattleUiState, obtainEvent: (BattleEvent) -> Unit){

    when(uiState){
        is BattleUiState.Loading -> { Connecting() }
        is BattleUiState.Error -> {}
        is BattleUiState.Success -> {
            Box(Modifier.fillMaxSize()) {
                Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    Battlefield(listBattlefield = uiState.yourCells, modifier = Modifier)
                    Text(text = uiState.yourMove.toString())
                    Battlefield(listBattlefield = uiState.friendCells, modifier = Modifier)
                }
            }

        }
    }

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


