package com.missclick.seabattle.presentation.screens.prepare

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.missclick.seabattle.R
import com.missclick.seabattle.presentation.components.BackMark
import com.missclick.seabattle.presentation.components.Battlefield
import com.missclick.seabattle.presentation.components.BattlefieldNew
import com.missclick.seabattle.presentation.components.listBattlefield
import com.missclick.seabattle.presentation.navigation.NavigationTree
import com.missclick.seabattle.presentation.ui.theme.AppTheme


@Composable
fun PrepareRoute(navController: NavController, vm: PrepareViewModel = hiltViewModel()) {

    val uiState by vm.uiState.collectAsState()

    PrepareScreen(
        uiState = uiState,
        obtainEvent = vm::obtainEvent,
        navigateTo = {
            println("navigate")
            navController.navigate(it) }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrepareScreen(
    uiState: PrepareUiState,
    obtainEvent: (PrepareEvent) -> Unit,
    navigateTo: (String) -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {
        val orientation = LocalConfiguration.current.orientation

        val battleFieldSize = listOf(
            LocalConfiguration.current.screenWidthDp,
            LocalConfiguration.current.screenHeightDp
        ).min() - 90

        when (orientation) {
            1 -> {
                //portrait
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BattlefieldNew(
                        listBattlefield = uiState.battleFieldListEnum, modifier = Modifier
                            .size(battleFieldSize.dp), obtainEvent, uiState.isCanGoBattle
                    )
                    Row(
                        Modifier
                            .size(battleFieldSize.dp)
                    ) {

                        Column(
                            modifier = Modifier
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Column(
                                Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.back_mark),
                                    modifier = Modifier
                                        .size((battleFieldSize / 10).dp)
                                        .rotate(90f)
                                        .clip(CircleShape)
                                        .clickable {
                                            obtainEvent(PrepareEvent.UpArrow)
                                        },
                                    contentDescription = null
                                )
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.back_mark),
                                        modifier = Modifier
                                            .size((battleFieldSize / 10).dp)
                                            .clip(CircleShape)
                                            .clickable {
                                                obtainEvent(PrepareEvent.LeftArrow)
                                            },
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.size((battleFieldSize / 5).dp))
                                    Image(
                                        painter = painterResource(id = R.drawable.back_mark),
                                        modifier = Modifier
                                            .size((battleFieldSize / 10).dp)
                                            .scale(scaleX = -1f, scaleY = 1f)
                                            .clip(CircleShape)
                                            .clickable {
                                                obtainEvent(PrepareEvent.RightArrow)
                                            },
                                        contentDescription = null
                                    )
                                }
                                Image(
                                    painter = painterResource(id = R.drawable.back_mark),
                                    modifier = Modifier
                                        .size((battleFieldSize / 10).dp)
                                        .rotate(-90f)
                                        .clip(CircleShape)
                                        .clickable {
                                            obtainEvent(PrepareEvent.DownArrow)
                                        },
                                    contentDescription = null
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.weight(7f))
                            Card(
                                modifier = Modifier
                                    .weight(4f)
                                    .width(battleFieldSize.dp / 3),
                                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                                border = BorderStroke(1.dp, AppTheme.colors.primary),
                                shape = RoundedCornerShape(50),
                                onClick = {
                                    obtainEvent(PrepareEvent.Random)
                                }
                            ) {
                                Box(Modifier.fillMaxSize()) {
                                    Text(
                                        text = "Random",
                                        modifier = Modifier.align(Alignment.Center),
                                        style = AppTheme.typography.h3
                                    )
                                }

                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Card(
                                modifier = Modifier
                                    .weight(4f)
                                    .width(battleFieldSize.dp / 3),
                                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                                border = BorderStroke(1.dp, AppTheme.colors.primary),
                                shape = RoundedCornerShape(50),
                                onClick = {
                                    obtainEvent(PrepareEvent.Roll)
                                }
                            ) {
                                Box(Modifier.fillMaxSize()) {
                                    Text(
                                        text = "Roll",
                                        modifier = Modifier.align(Alignment.Center),
                                        style = AppTheme.typography.h3
                                    )
                                }

                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Card(
                                modifier = Modifier
                                    .weight(4f)
                                    .width(battleFieldSize.dp / 3)
                                    .alpha(if (uiState.isCanGoBattle) 1f else 0.3f)
                                ,
                                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                                border = BorderStroke(1.dp, AppTheme.colors.primary),
                                onClick = {
                                    obtainEvent(PrepareEvent.Battle)
                                    println("click card")
                                    navigateTo(NavigationTree.Battle.route + "/" + uiState.code + "/" + uiState.isOwner.toString())
                                }
                            ) {
                                Box(Modifier.fillMaxSize()) {
                                    Text(
                                        text = "Battle!",
                                        modifier = Modifier.align(Alignment.Center),
                                        style = AppTheme.typography.h3
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.weight(7f))
                        }
                    }
                }
            }

            2 -> {
                //landscape
                Row(
                    Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    BattlefieldNew(
                        listBattlefield = uiState.battleFieldListEnum, modifier = Modifier
                            .size(battleFieldSize.dp), obtainEvent, uiState.isCanGoBattle
                    )
                    Row(
                        Modifier
                            .size(battleFieldSize.dp)
                    ) {

                        Column(
                            modifier = Modifier
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Column(
                                Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.back_mark),
                                    modifier = Modifier
                                        .size((battleFieldSize / 10).dp)
                                        .rotate(90f)
                                        .clip(CircleShape)
                                        .clickable {
                                            obtainEvent(PrepareEvent.UpArrow)
                                        },
                                    contentDescription = null
                                )
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.back_mark),
                                        modifier = Modifier
                                            .size((battleFieldSize / 10).dp)
                                            .clip(CircleShape)
                                            .clickable {
                                                obtainEvent(PrepareEvent.LeftArrow)
                                            },
                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.size((battleFieldSize / 5).dp))
                                    Image(
                                        painter = painterResource(id = R.drawable.back_mark),
                                        modifier = Modifier
                                            .size((battleFieldSize / 10).dp)
                                            .scale(scaleX = -1f, scaleY = 1f)
                                            .clip(CircleShape)
                                            .clickable {
                                                obtainEvent(PrepareEvent.RightArrow)
                                            },
                                        contentDescription = null
                                    )
                                }
                                Image(
                                    painter = painterResource(id = R.drawable.back_mark),
                                    modifier = Modifier
                                        .size((battleFieldSize / 10).dp)
                                        .rotate(-90f)
                                        .clip(CircleShape)
                                        .clickable {
                                            obtainEvent(PrepareEvent.DownArrow)
                                        },
                                    contentDescription = null
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.weight(7f))
                            Card(
                                modifier = Modifier
                                    .weight(4f)
                                    .width(battleFieldSize.dp / 3),
                                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                                border = BorderStroke(1.dp, AppTheme.colors.primary),
                                shape = RoundedCornerShape(50),
                                onClick = {
                                    obtainEvent(PrepareEvent.Random)
                                }
                            ) {
                                Box(Modifier.fillMaxSize()) {
                                    Text(
                                        text = "Random",
                                        modifier = Modifier.align(Alignment.Center),
                                        style = AppTheme.typography.h3
                                    )
                                }

                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Card(
                                modifier = Modifier
                                    .weight(4f)
                                    .width(battleFieldSize.dp / 3),
                                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                                border = BorderStroke(1.dp, AppTheme.colors.primary),
                                shape = RoundedCornerShape(50),
                                onClick = {
                                    obtainEvent(PrepareEvent.Roll)
                                }
                            ) {
                                Box(Modifier.fillMaxSize()) {
                                    Text(
                                        text = "Roll",
                                        modifier = Modifier.align(Alignment.Center),
                                        style = AppTheme.typography.h3
                                    )
                                }

                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Card(
                                modifier = Modifier
                                    .weight(4f)
                                    .width(battleFieldSize.dp / 3)
                                    .alpha(if (uiState.isCanGoBattle) 1f else 0.3f)
                                ,
                                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                                border = BorderStroke(1.dp, AppTheme.colors.primary),
                                onClick = {
                                    obtainEvent(PrepareEvent.Battle)
                                    println("click card")
                                    navigateTo(NavigationTree.Battle.route + "/" + uiState.code + "/" + uiState.isOwner.toString())
                                }
                            ) {
                                Box(Modifier.fillMaxSize()) {
                                    Text(
                                        text = "Battle!",
                                        modifier = Modifier.align(Alignment.Center),
                                        style = AppTheme.typography.h3
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.weight(7f))
                        }
                    }
                }

            }
        }



        BackMark {

        }
    }

}