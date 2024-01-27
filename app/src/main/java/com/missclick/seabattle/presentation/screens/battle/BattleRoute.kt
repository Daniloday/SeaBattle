package com.missclick.seabattle.presentation.screens.battle

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.missclick.seabattle.R
import com.missclick.seabattle.domain.model.Cell
import com.missclick.seabattle.presentation.components.BackMark
import com.missclick.seabattle.presentation.components.Battlefield
import com.missclick.seabattle.presentation.components.Connecting
import com.missclick.seabattle.presentation.components.listBattlefield
import com.missclick.seabattle.presentation.ui.theme.SeaBattleTheme

@Composable
fun BattleRoute(
    navController: NavHostController,
    battleViewModel: BattleViewModel = hiltViewModel()
) {


    val uiState by battleViewModel.uiState.collectAsState()

    BattleScreen(uiState = uiState, obtainEvent = battleViewModel::obtainEvent)


}

@Composable
fun BattleScreen(uiState: BattleUiState, obtainEvent: (BattleEvent) -> Unit) {

    when (uiState) {
        is BattleUiState.Loading -> {
            Connecting()
        }

        is BattleUiState.Error -> {}
        is BattleUiState.Success -> {
            BattleSuccess(uiState, obtainEvent)
        }
    }

}



@Composable
fun BattleSuccess(uiState: BattleUiState.Success, obtainEvent: (BattleEvent) -> Unit) {
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



        BackMark {

        }
    }
}






