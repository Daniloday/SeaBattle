package com.missclick.seabattle.presentation.screens.multiplayer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.missclick.seabattle.R
import com.missclick.seabattle.presentation.navigation.NavigationTree
import com.missclick.seabattle.presentation.screens.menu.MenuEvent

import com.missclick.seabattle.presentation.screens.menu.MenuUiState
import com.missclick.seabattle.presentation.screens.menu.MenuViewModel
import com.missclick.seabattle.presentation.ui.theme.AppTheme

@Composable
fun MultiplayerRoute(navController: NavController, vm: MultiplayerViewModel = hiltViewModel()) {

    val uiState by vm.uiState.collectAsState()

    MultiplayerScreen(
        uiState = uiState,
        obtainEvent = vm::obtainEvent,
        navigateTo = { navController.navigate(it.route) }
    )

}

@Composable
fun MultiplayerScreen(
    uiState: MultiplayerUiState,
    obtainEvent: (MultiplayerEvent) -> Unit,
    navigateTo: (NavigationTree) -> Unit
) {
    var text by remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly

        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text(stringResource(id = R.string.label)) }
            )
            Text(text = stringResource(id = R.string.or), modifier = Modifier.clickable {

            },
                style = AppTheme.typography.headerTextBold,
                color = AppTheme.colors.primary
            )
            Text(text = stringResource(id = R.string.create_room), modifier = Modifier.clickable {

            },
                style = AppTheme.typography.headerTextBold,
                color = AppTheme.colors.primary
            )

        }
    }
}