package com.missclick.seabattle.presentation.screens.multiplayer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.missclick.seabattle.R
import com.missclick.seabattle.presentation.components.BackMark
import com.missclick.seabattle.presentation.navigation.NavigationTree
import com.missclick.seabattle.presentation.screens.menu.MenuEvent

import com.missclick.seabattle.presentation.screens.menu.MenuUiState
import com.missclick.seabattle.presentation.screens.menu.MenuViewModel
import com.missclick.seabattle.presentation.ui.theme.AppTheme

@Composable
fun MultiplayerRoute(navController: NavController, vm: MultiplayerViewModel = hiltViewModel()) {

    val uiState by vm.uiState.collectAsState()

    if (uiState.connectionStatus is ConnectionStatus.Success) {
        LaunchedEffect(key1 = Unit, block = {
            navController.navigate(NavigationTree.Battle.route + "/" + uiState.code + "/" + "false")
        })
    }

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

    val pattern = remember { Regex("^\\d+\$") }


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize(0.7F)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly

        ) {
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextField(
                    value = uiState.code,
                    singleLine = true,
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(10.dp))
                        .border(
                            1.dp,
                            color = when (uiState.connectionStatus) {
                                is ConnectionStatus.Nothing, ConnectionStatus.Loading -> AppTheme.colors.primary
                                is ConnectionStatus.Error -> AppTheme.colors.error
                                is ConnectionStatus.Success -> AppTheme.colors.success
                            },
                            shape = RoundedCornerShape(10.dp)
                        )
                        .background(AppTheme.colors.secondaryBackground)
                        .width(width = 230.dp),
                    textStyle = AppTheme.typography.h3,
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.label),
                            color = AppTheme.colors.secondary,
                            style = AppTheme.typography.h3
                        )
                    },
                    onValueChange = {
                        if (it.isEmpty() || it.matches(pattern)) {
                            if (it.length <= 5){
                                obtainEvent(MultiplayerEvent.OnCodeChange(it))
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        autoCorrect = false
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { obtainEvent(MultiplayerEvent.Connect) },
                    )
                )


                if (uiState.connectionStatus is ConnectionStatus.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(start = 24.dp)
                            .size(36.dp)
                            .align(Alignment.CenterVertically), color = AppTheme.colors.secondary
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.select_mark),
                        modifier = Modifier
                            .padding(start = 24.dp)
                            .size(36.dp)
                            .align(Alignment.CenterVertically)
                            .clickable {
                                obtainEvent(MultiplayerEvent.Connect)
                            },
                        contentDescription = null,
                    )
                }

            }

            Text(
                text = stringResource(id = R.string.or), modifier = Modifier.clickable {

                },
                style = AppTheme.typography.h2,
                color = AppTheme.colors.secondary
            )
            Text(
                text = stringResource(id = R.string.create_room), modifier = Modifier.clickable {
                    navigateTo(NavigationTree.Waiting)
                },
                style = AppTheme.typography.h2,
                color = AppTheme.colors.primary
            )
        }
        BackMark() {
            navigateTo(NavigationTree.Menu)
        }
    }
}