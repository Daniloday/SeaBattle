package com.missclick.seabattle.presentation.screens.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.missclick.seabattle.presentation.ui.theme.SeaBattleTheme

@Composable
fun MenuRoute(vm : MenuViewModel = hiltViewModel() ){

    val uiState by vm.uiState.collectAsState()

    MenuScreen(
        obtainEvent = vm::obtainEvent,
        uiState = uiState
    )

}

@Composable
fun MenuScreen(
    obtainEvent : (MenuEvent) -> Unit,
    uiState: MenuUiState
){

    Box(modifier = Modifier
        .size(100.dp)
        .background(Color.Gray)){
        Text(text = uiState.a, color = Color.Red)
    }
    
}