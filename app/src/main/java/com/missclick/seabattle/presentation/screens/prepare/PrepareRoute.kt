package com.missclick.seabattle.presentation.screens.prepare

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.missclick.seabattle.presentation.navigation.NavigationTree


@Composable
fun PrepareRoute(navController: NavController, vm: PrepareViewModel = hiltViewModel()) {

    val uiState by vm.uiState.collectAsState()

    PrepareScreen(
        uiState = uiState,
        obtainEvent = vm::obtainEvent,
        navigateTo = { navController.navigate(it.route) }
    )

}

@Composable
fun PrepareScreen(
    uiState: PrepareUiState,
    obtainEvent: (PrepareEvent) -> Unit,
    navigateTo: (NavigationTree) -> Unit
) {



}