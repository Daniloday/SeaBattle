package com.missclick.seabattle.data.remote.dto

import com.missclick.seabattle.domain.model.Cell
import com.missclick.seabattle.domain.model.Game

fun generateEmptyField() : List<CellDto>{
    val list = mutableListOf<CellDto>()
    repeat(100){
        list.add(CellDto(dot = false, ship = false))
    }
    return list
}
data class GameDto(
    val ownerCells : List<CellDto> = generateEmptyField(),
    val friendCells : List<CellDto> = generateEmptyField(),
    val friendIsConnected : Boolean = false,
    val ownerIsConnected : Boolean = false,
    val ownerIsReady : Boolean = false,
    val friendIsReady : Boolean = false,
    val moveOwner : Boolean = true

)

data class CellDto(
    val dot : Boolean,
    val ship : Boolean
)



fun GameDto.toGame(isOwner : Boolean) : Game{

    return Game(
        yourCells = if (isOwner) ownerCells.dtoCellsToGame() else friendCells.dtoCellsToGame(),
        friendCells = if (!isOwner) ownerCells.dtoCellsToGame() else friendCells.dtoCellsToGame(),
        friendIsConnected = if (isOwner) friendIsConnected else ownerIsConnected,
        youAreConnected = if (isOwner) ownerIsConnected else friendIsConnected,
        friendIsReady = if (isOwner) friendIsReady else ownerIsReady,
        youAreReady = if (isOwner) ownerIsReady else friendIsReady,
        yourMove = if (isOwner) moveOwner else !moveOwner,
        youAreWinner = when{
            ownerCells.count { it.ship && it.dot } == 20 -> !isOwner
            friendCells.count { it.ship && it.dot } == 20 -> isOwner
            else -> null
        }
    )
}

fun Game.toDto(isOwner : Boolean) : GameDto{
    return GameDto(
        ownerCells = if (isOwner) yourCells.cellsToDto() else friendCells.cellsToDto(),
        friendCells = if (!isOwner) yourCells.cellsToDto() else friendCells.cellsToDto(),
        friendIsConnected = if (isOwner) friendIsConnected else youAreConnected,
        ownerIsConnected = if (isOwner) youAreConnected else friendIsConnected,
        ownerIsReady = if (isOwner) youAreReady else friendIsReady,
        friendIsReady = if (isOwner) friendIsReady else youAreReady,
        moveOwner = if (isOwner) yourMove else !yourMove
    )
}

fun List<CellDto>.dtoCellsToGame() : List<List<Cell>>{
    try {
        val cells = mutableListOf<List<Cell>>()
        repeat(10){ index1 ->
            val cellRow = mutableListOf<Cell>()
            repeat(10){ index2 ->
                val cellDto = this[index1 * 10 + index2]
                cellRow.add(
                    when{
                        !cellDto.ship && cellDto.dot -> Cell.DOT
                        cellDto.ship && !cellDto.dot -> Cell.SHIP_ALIVE
                        cellDto.ship && cellDto.dot -> Cell.SHIP_DAMAGE
                        else -> Cell.EMPTY
                    })
            }
            cells.add(cellRow)
        }
        return cells
    }catch (e : Throwable){
        return emptyList()
    }

}

fun List<List<Cell>>.cellsToDto() : List<CellDto>{
    try {
        val listDto = mutableListOf<CellDto>()
        forEach {
            it.forEach{ cell ->
                listDto.add(when(cell){
                    Cell.EMPTY -> CellDto(dot = false, ship = false)
                    Cell.DOT -> CellDto(dot = true, ship = false)
                    Cell.SHIP_ALIVE -> CellDto(dot = false, ship = true)
                    Cell.SHIP_DAMAGE -> CellDto(dot = true, ship = true)

                })
            }
        }
        return listDto
    }catch (e : Throwable){
        return emptyList()
    }

}

