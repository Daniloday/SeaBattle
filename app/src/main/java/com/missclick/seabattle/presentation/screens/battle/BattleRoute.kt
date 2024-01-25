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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.missclick.seabattle.presentation.components.Battlefield
import com.missclick.seabattle.presentation.components.Connecting

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

