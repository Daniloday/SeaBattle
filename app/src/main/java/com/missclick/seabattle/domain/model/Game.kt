package com.missclick.seabattle.domain.model

import com.missclick.seabattle.presentation.screens.battle.BattleUiState

data class Game(
    val yourCells : List<List<Cell>>,
    val friendCells : List<List<Cell>>,
    val friendIsConnected : Boolean = false,
    val youAreConnected : Boolean = false,
    val friendIsReady : Boolean = false,
    val youAreReady : Boolean = false,
    val yourMove : Boolean = true,
    val youAreWinner : Boolean? = null
)


