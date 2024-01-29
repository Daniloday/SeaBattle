package com.missclick.seabattle.presentation.screens.battle

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.missclick.seabattle.R
import com.missclick.seabattle.domain.model.Cell
import com.missclick.seabattle.presentation.components.BackMark
import com.missclick.seabattle.presentation.components.Battlefield
import com.missclick.seabattle.presentation.components.Connecting
import com.missclick.seabattle.presentation.navigation.NavigationTree
import com.missclick.seabattle.presentation.ui.theme.AppTheme

@Composable
fun BattleRoute(
    navController: NavHostController,
    battleViewModel: BattleViewModel = hiltViewModel()
) {





    val uiState by battleViewModel.uiState.collectAsState()

    BattleScreen(uiState = uiState, obtainEvent = battleViewModel::obtainEvent,
        navigateTo = { navController.navigate(it)},
        code = battleViewModel.code,
        isOwner = battleViewModel.isOwner
    )


}

@Composable
fun BattleScreen(uiState: BattleUiState, obtainEvent: (BattleEvent) -> Unit, navigateTo : (String) -> Unit,
                 code : String, isOwner : Boolean) {



    when (uiState) {
        is BattleUiState.Loading -> {
            Connecting()
        }

        is BattleUiState.Error -> {
            Text(text = "Error", style = AppTheme.typography.h4, color = AppTheme.colors.error)
        }
        is BattleUiState.Success -> {
            if (uiState.youAreConnected && uiState.friendsIsConnected){
                navigateTo(NavigationTree.Prepare.route + "/" + code + "/" + isOwner.toString())
            }

            BattleSuccess(uiState = uiState, obtainEvent = obtainEvent, navigateTo = navigateTo, code = code, isOwner = isOwner )
        }
    }

}

@Composable
fun BattleSuccess(uiState: BattleUiState.Success, obtainEvent: (BattleEvent) -> Unit, navigateTo : (String) -> Unit, code : String, isOwner : Boolean) {
    Box(modifier = Modifier.fillMaxSize()) {

        val orientation = LocalConfiguration.current.orientation

        val battleFieldSize = listOf(
            LocalConfiguration.current.screenWidthDp,
            LocalConfiguration.current.screenHeightDp
        ).min() - 90

        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                //portrait
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Battlefield(
                        listBattlefield = uiState.yourCells, modifier = Modifier
                            .size(battleFieldSize.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.back_mark),
                        contentDescription = "mark",
                        modifier = Modifier
                            .size(40.dp)
                            .rotate(
                                if (uiState.yourMove) -90f else 90f
                            )
                    )
                    Battlefield(
                        listBattlefield = uiState.friendCells
                        .map {row ->
                                                                  row.map {
                                                                      if (it == Cell.SHIP_ALIVE){
                                                                          Cell.EMPTY
                                                                      }else{
                                                                          it
                                                                      }
                                                                  }
                        }
                , modifier = Modifier
                            .size(battleFieldSize.dp),
                        onClick = if (uiState.yourMove) { y, x ->
                            obtainEvent(BattleEvent.DoStep(y = y, x = x))
                        } else null
                    )
                }
            }

            Configuration.ORIENTATION_LANDSCAPE -> {
                //landscape
                Row(
                    Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Battlefield(
                        listBattlefield = uiState.yourCells, modifier = Modifier
                            .size(battleFieldSize.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.back_mark),
                        contentDescription = "mark",
                        modifier = Modifier
                            .size(40.dp)
                            .rotate(
                                if (uiState.yourMove) 180f else 0f
                            )
                    )
                    Battlefield(
                        listBattlefield = uiState.friendCells, modifier = Modifier
                            .size(battleFieldSize.dp),
                        onClick = if (uiState.yourMove) { y, x ->
                            obtainEvent(BattleEvent.DoStep(y = y, x = x))
                        } else null
                    )
                }

            }

            else -> {}
        }

        if (uiState.youAreWinner != null){
            WinDialog(uiState = uiState, navigateTo = navigateTo, code = code, isOwner = isOwner, obtainEvent = obtainEvent)
        }


        BackMark {

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WinDialog(uiState: BattleUiState.Success, navigateTo : (String) -> Unit, code : String, isOwner : Boolean, obtainEvent: (BattleEvent) -> Unit,){

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xA6222222))) {
        
        Box(modifier = Modifier
            .fillMaxWidth(0.7f)
            .fillMaxHeight(0.4f)
            .align(Alignment.Center)
            .clip(RoundedCornerShape(10.dp))
            .background(AppTheme.colors.secondaryBackground)
            ){

            Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                if (uiState.youAreWinner == true){
                    Text(text = stringResource(id = R.string.youWon),
                        style = AppTheme.typography.h2,
                        color = AppTheme.colors.success
                    )
                }else{
                    Text(text = stringResource(id = R.string.youLost),
                        style = AppTheme.typography.h2,
                        color = AppTheme.colors.error
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))


                if (uiState.youAreConnected){
                    Text(text = stringResource(id = R.string.waiting),
                        style = AppTheme.typography.h1,
                        color = AppTheme.colors.secondary,
                    )
                }else{
                    Text(text = stringResource(id = R.string.restart),
                        style = AppTheme.typography.h1,
                        color = AppTheme.colors.primary,
                        modifier = Modifier.clickable {
                            obtainEvent(BattleEvent.Restart)
                        }
                    )
                }


                Spacer(modifier = Modifier.height(16.dp))

                Text(text = stringResource(id = R.string.menu),
                    style = AppTheme.typography.h1,
                    color = AppTheme.colors.primary,
                    modifier = Modifier.clickable {
                        obtainEvent(BattleEvent.Exit)
                        navigateTo(NavigationTree.Menu.route)
                    }
                )


            }

            
        }
        
    }


}






