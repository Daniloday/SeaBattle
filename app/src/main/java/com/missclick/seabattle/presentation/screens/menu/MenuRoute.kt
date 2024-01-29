package com.missclick.seabattle.presentation.screens.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.missclick.seabattle.R
import com.missclick.seabattle.presentation.navigation.NavigationTree
import com.missclick.seabattle.presentation.ui.theme.AppTheme

@Composable
fun MenuRoute(navController: NavController, vm: MenuViewModel = hiltViewModel()) {

    val uiState by vm.uiState.collectAsState()

    MenuScreen(
        uiState = uiState,
        obtainEvent = vm::obtainEvent
    ) { navController.navigate(it) }

}

@Composable
fun MenuScreen(
    uiState: MenuUiState,
    obtainEvent: (MenuEvent) -> Unit,
    navigateTo: (String) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly

        ) {

        Text(text = stringResource(id = R.string.single_mode), modifier = Modifier.clickable {

        },
            style = AppTheme.typography.h1,
            color = AppTheme.colors.primary
        )
        Text(text = stringResource(id = R.string.multiplayer), modifier = Modifier.clickable {
            navigateTo(NavigationTree.Prepare.route + "/" + "87871" + "/" + "true")
        },
            style = AppTheme.typography.h1,
            color = AppTheme.colors.primary
        )
        Text(text = stringResource(id = R.string.settings), modifier = Modifier.clickable {


        },
            style = AppTheme.typography.h1,
            color = AppTheme.colors.primary
        )

    }

}