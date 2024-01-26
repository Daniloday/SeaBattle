package com.missclick.seabattle.presentation.screens.prepare.models

data class CellStatePrepare(
    var isEnabled: Boolean,
    var isShip: Boolean
)

data class CellPosition(
    var row: Int,
    var column: Int
)

data class ShipsDataClass(
    var isSelected: Boolean,
    var shipCells: List<CellPosition>
)
