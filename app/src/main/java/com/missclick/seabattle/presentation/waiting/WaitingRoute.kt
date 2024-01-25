package com.missclick.seabattle.presentation.waiting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.missclick.seabattle.R
import com.missclick.seabattle.presentation.components.Connecting
import com.missclick.seabattle.presentation.navigation.NavigationTree
import com.missclick.seabattle.presentation.ui.theme.AppTheme

@Composable
fun WaitingRoute(navController: NavController, vm: WaitingViewModel = hiltViewModel()) {

    val uiState by vm.uiState.collectAsState()

    WaitingScreen(uiState) {
        navController.navigate(it.route)
    }
}

@Composable
fun WaitingScreen(uiState: WaitingUiState, navigateTo: (NavigationTree) -> Unit) {


    when (uiState) {
        is WaitingUiState.Loading -> {
            Connecting()
        }
        is WaitingUiState.Error -> {} //todo do it
        is WaitingUiState.Success -> {
            WaitingSuccess(uiState, navigateTo)
        }

    }

}

@Composable
fun WaitingSuccess(uiState: WaitingUiState.Success, navigateTo: (NavigationTree) -> Unit) {
    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(68.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = uiState.code,
                color = AppTheme.colors.primary,
                style = AppTheme.typography.h4
            )

            Text(
                text = stringResource(id = R.string.shareCode),
                color = AppTheme.colors.secondary,
                style = AppTheme.typography.h3
            )
        }

        if (uiState.friendIsConnected) {
            LaunchedEffect(key1 = Unit, block = {
                println("navigate from waiting")
                navigateTo(NavigationTree.Prepare)
            })
        }
    }
}