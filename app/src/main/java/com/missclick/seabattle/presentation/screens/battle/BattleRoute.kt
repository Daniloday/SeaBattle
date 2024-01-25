package com.missclick.seabattle.presentation.screens.battle

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
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
            BattleSuccess(uiState, obtainEvent)
        }
    }

}


@Composable
fun BattleSuccess(uiState: BattleUiState.Success, obtainEvent: (BattleEvent) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {

        val orientation = LocalConfiguration.current.orientation

        val battleFieldSize = listOf(LocalConfiguration.current.screenWidthDp, LocalConfiguration.current.screenHeightDp).min() - 90

        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                //portrait
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Battlefield(
                        listBattlefield = uiState.yourCells, modifier = Modifier
                            .size(battleFieldSize.dp)
                    )
                    Text(text = uiState.yourMove.toString())
                    Battlefield(
                        listBattlefield = uiState.friendCells, modifier = Modifier
                            .size(battleFieldSize.dp)
                    )
                }
            }

            Configuration.ORIENTATION_LANDSCAPE -> {
                //landscape
                Row(
                    Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Battlefield(
                        listBattlefield = uiState.yourCells, modifier = Modifier
                            .size(battleFieldSize.dp)
                    )
                    Text(text = uiState.yourMove.toString())
                    Battlefield(
                        listBattlefield = uiState.friendCells, modifier = Modifier
                            .size(battleFieldSize.dp)
                    )
                }

            }

            else -> {}
        }



        BackMark {

        }
    }
}




