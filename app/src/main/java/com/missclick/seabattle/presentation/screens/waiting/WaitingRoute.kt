package com.missclick.seabattle.presentation.screens.waiting

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.missclick.seabattle.R
import com.missclick.seabattle.presentation.components.BackMark
import com.missclick.seabattle.presentation.components.Connecting
import com.missclick.seabattle.presentation.components.click
import com.missclick.seabattle.presentation.navigation.NavigationTree
import com.missclick.seabattle.presentation.ui.theme.AppTheme

@Composable
fun WaitingRoute(navController: NavController, vm: WaitingViewModel = hiltViewModel()) {

    val uiState by vm.uiState.collectAsState()

    WaitingScreen(
        uiState = uiState, navigateTo = { navController.navigate(it) },
        obtainEvent = vm::obtainEvent,
        navigateBack = { navController.popBackStack() }
    )

    BackHandler {
        vm.obtainEvent(WaitingEvent.Exit)
        navController.popBackStack()
    }






}

@Composable
fun WaitingScreen(
    uiState: WaitingUiState, navigateTo: (String) -> Unit,
    obtainEvent: (WaitingEvent) -> Unit,
    navigateBack : () -> Unit
) {

    Box(Modifier.fillMaxSize()) {

        BackMark {
            obtainEvent(WaitingEvent.Exit)
            navigateBack()
        }

        when (uiState) {
            is WaitingUiState.Loading -> {
                Connecting(modifier = Modifier.align(Alignment.Center))
            }

            is WaitingUiState.Error -> {
                WaitingError(
                    modifier = Modifier.align(Alignment.Center),
                    obtainEvent = obtainEvent
                )
            }

            is WaitingUiState.Success -> {
                WaitingSuccess(
                    modifier = Modifier.align(Alignment.Center),
                    uiState = uiState,
                    navigateTo = navigateTo,
                    obtainEvent = obtainEvent
                )
            }

        }

    }



}

@Composable
fun WaitingSuccess(
    modifier: Modifier = Modifier,
    uiState: WaitingUiState.Success, navigateTo: (String) -> Unit,
    obtainEvent: (WaitingEvent) -> Unit
) {

    val context = LocalContext.current


        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(68.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = uiState.code,
                color = AppTheme.colors.primary,
                style = AppTheme.typography.h4,
                modifier = Modifier.click {
                    obtainEvent(WaitingEvent.Copy(context))
                }
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
                navigateTo(NavigationTree.Prepare.route + "/" + uiState.code + "/" + "true")
            })
        }

}

@Composable
fun WaitingError(
    modifier: Modifier = Modifier,
    obtainEvent: (WaitingEvent) -> Unit
){

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(68.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(id = R.string.error),
            color = AppTheme.colors.error,
            style = AppTheme.typography.h4,

        )

        Text(
            text = stringResource(id = R.string.tryAgain),
            color = AppTheme.colors.primary,
            style = AppTheme.typography.h1,
            modifier = Modifier.click {
                obtainEvent(WaitingEvent.Reconnect)
            }
        )

    }


}