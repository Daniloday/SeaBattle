package com.missclick.seabattle.presentation.screens.prepare

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.missclick.seabattle.presentation.components.BackMark
import com.missclick.seabattle.presentation.components.Battlefield
import com.missclick.seabattle.presentation.components.listBattlefield
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

    Box(modifier = Modifier.fillMaxSize()) {
        val orientation = LocalConfiguration.current.orientation

        println(orientation)


        when (orientation) {
            1 -> {
                //portrait
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Battlefield(
                        listBattlefield = listBattlefield, modifier = Modifier
                            .size(300.dp)
                    )
                    Battlefield(
                        listBattlefield = listBattlefield, modifier = Modifier
                            .size(300.dp)
                    )
                }
            }

            2 -> {
                //landscape
                Row(
                    Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Battlefield(
                        listBattlefield = listBattlefield, modifier = Modifier
                            .size(300.dp)
                    )
                    Battlefield(
                        listBattlefield = listBattlefield, modifier = Modifier
                            .size(300.dp)
                    )
                }

            }
        }



        BackMark {

        }
    }

}