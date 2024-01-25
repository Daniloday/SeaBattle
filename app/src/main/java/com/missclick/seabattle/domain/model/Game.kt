package com.missclick.seabattle.domain.model

import com.missclick.seabattle.presentation.screens.battle.BattleUiState

data class Game(
    val yourCells : List<List<Cell>>,
    val friendCells : List<List<Cell>>,
    val code : String,
    val friendIsConnected : Boolean = false,
    val friendIsReady : Boolean = false,
    val youAreReady : Boolean = false,
    val yourMove : Boolean = true,
)


