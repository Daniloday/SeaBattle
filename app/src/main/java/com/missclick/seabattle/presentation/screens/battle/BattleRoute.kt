package com.missclick.seabattle.presentation.screens.battle

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun BattleRoute(navController: NavHostController, battleViewModel: BattleViewModel) {


    val uiState by battleViewModel.uiState.collectAsState()

    when(val uiStateCurrent = uiState){
        is BattleUiState.Loading -> {
            CircularProgressIndicator()}
        is BattleUiState.Error -> {
            Text(text = uiStateCurrent.errorName, color = Color.Red, fontSize = 30.sp)
        }
        is BattleUiState.Success -> {
            Text(text = uiStateCurrent.field, color = Color.Green,  fontSize = 30.sp)
            }
        }


}