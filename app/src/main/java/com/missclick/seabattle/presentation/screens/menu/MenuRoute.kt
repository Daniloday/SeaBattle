package com.missclick.seabattle.presentation.screens.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.missclick.seabattle.presentation.navigation.NavigationTree
import com.missclick.seabattle.presentation.ui.theme.SeaBattleTheme

@Composable
fun MenuRoute(navController: NavController, vm : MenuViewModel = hiltViewModel() ){

    val uiState by vm.uiState.collectAsState()

    MenuScreen(
        uiState = uiState,
        obtainEvent = vm::obtainEvent,
        navigateTo = {navController.navigate(it.route)}
    )

}

@Composable
fun MenuScreen(
    uiState: MenuUiState,
    obtainEvent : (MenuEvent) -> Unit,
    navigateTo : (NavigationTree) -> Unit
){

    Box(modifier = Modifier
        .size(100.dp)
        .background(Color.Gray)
        .clickable { navigateTo(NavigationTree.Menu) }){
        Text(text = uiState.a, color = Color.Red)
    }
    
}