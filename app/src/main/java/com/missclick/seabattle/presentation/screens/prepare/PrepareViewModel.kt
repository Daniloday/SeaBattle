package com.missclick.seabattle.presentation.screens.prepare

import com.missclick.seabattle.common.BaseViewModel
import com.missclick.seabattle.domain.model.Cell
import com.missclick.seabattle.domain.model.CellPrepare
import com.missclick.seabattle.presentation.screens.prepare.models.CellPosition
import com.missclick.seabattle.presentation.screens.prepare.models.CellStatePrepare
import com.missclick.seabattle.presentation.screens.prepare.models.ShipsDataClass
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrepareViewModel @Inject constructor() :
    BaseViewModel<PrepareUiState, PrepareEvent>(PrepareUiState()) {


    override fun obtainEvent(event: PrepareEvent) {
        when (event) {
            is PrepareEvent.UpArrow -> {
                upArrow()
            }

            is PrepareEvent.DownArrow -> {
                downArrow()
            }

            is PrepareEvent.LeftArrow -> {
                leftArrow()
            }

            is PrepareEvent.RightArrow -> {
                rightArrow()
            }

            is PrepareEvent.Random -> {
                random()
            }

            is PrepareEvent.Roll -> {
                roll()
            }

            is PrepareEvent.Battle -> {
                battle()
            }

            is PrepareEvent.ClickOnCell -> {
                clickOnCell(event.row, event.column)
            }
        }
    }

    init {

        random()
    }

    private fun upArrow() {
        val ship: CellPosition

//        uiState.value.shipsList.forEach {
//
//        }

    }
    private fun downArrow() {}
    private fun leftArrow() {}
    private fun rightArrow() {}

    private fun isPossibleToMove(
        direction: String, listCellsToMove: MutableList<CellPosition>
    ): Boolean {

        when (direction) {
            "up" -> {
                listCellsToMove.forEach { it ->
                    if (it.row - 1 in 0 until 10) return true
                }
            }

            "down" -> {
                listCellsToMove.forEach { it ->
                    if (it.row + 1 in 0 until 10) return true
                }
            }

            "left" -> {
                listCellsToMove.forEach { it ->
                    if (it.column - 1 in 0 until 10) return true
                }
            }

            "right" -> {
                listCellsToMove.forEach { it ->
                    if (it.column + 1 in 0 until 10) return true
                }
            }
        }

        return false
    }

    private fun random() {

        var battleFieldList: MutableList<MutableList<CellStatePrepare>> = mutableListOf()

        //make empty battlefield

        for (i in 0 until 10) {
            battleFieldList.add(mutableListOf())
            for (j in 0 until 10) {
                battleFieldList[i].add(CellStatePrepare(true, false))
            }
        }

        _uiState.value = uiState.value.copy(
            battleFieldList = battleFieldList, battleFieldListEnum = battleFieldList.map {
                it.map { it2 ->
                    if (it2.isShip) CellPrepare.SHIP_ALIVE else CellPrepare.EMPTY
                }
            }, shipsList = listOf(
                listOf(
                    ShipsDataClass(
                        isSelected = false, listOf(
                            CellPosition(-1, -1),
                            CellPosition(-1, -1),
                            CellPosition(-1, -1),
                            CellPosition(-1, -1)
                        )
                    )
                ), listOf(
                    ShipsDataClass(
                        isSelected = false, listOf(
                            CellPosition(-1, -1), CellPosition(-1, -1), CellPosition(-1, -1)
                        )
                    ), ShipsDataClass(
                        isSelected = false, listOf(
                            CellPosition(-1, -1),
                            CellPosition(-1, -1),
                            CellPosition(-1, -1),

                            )

                    )
                ), listOf(
                    ShipsDataClass(
                        isSelected = false, listOf(
                            CellPosition(-1, -1), CellPosition(-1, -1)
                        )

                    ), ShipsDataClass(
                        isSelected = false, listOf(
                            CellPosition(-1, -1), CellPosition(-1, -1)
                        )
                    ), ShipsDataClass(
                        isSelected = false, listOf(
                            CellPosition(-1, -1), CellPosition(-1, -1)
                        )
                    )
                ), listOf(
                    ShipsDataClass(
                        isSelected = false, listOf(
                            CellPosition(-1, -1)
                        )
                    ),

                    ShipsDataClass(
                        isSelected = false, listOf(
                            CellPosition(-1, -1)
                        )
                    ),

                    ShipsDataClass(
                        isSelected = false, listOf(
                            CellPosition(-1, -1)
                        )
                    ),

                    ShipsDataClass(
                        isSelected = false, listOf(
                            CellPosition(-1, -1)
                        )
                    )
                )

            ), isCanGoBattle = false
        )


        //start to fill cells

        //add 4-cells ship
        addShipOnBattleField(4, 0, 0)

        //add 3-cells ships
        addShipOnBattleField(3, 1, 0)
        addShipOnBattleField(3, 1, 1)

        //add 2-cells ships
        addShipOnBattleField(2, 2, 0)
        addShipOnBattleField(2, 2, 1)
        addShipOnBattleField(2, 2, 2)


        //add 1-cells ships
        addShipOnBattleField(1, 3, 0)
        addShipOnBattleField(1, 3, 1)
        addShipOnBattleField(1, 3, 2)
        addShipOnBattleField(1, 3, 3)

        //checker that all ships on field

        println(readyToBattle())

//        _uiState.value = uiState.value.copy(
//            isCanGoBattle = readyToBattle()
//        )

    }

    private fun addShipOnBattleField(shipSize: Int, deck: Int, numberOfShip: Int) {

        var battleFieldList =
            uiState.value.battleFieldList.map { it.toMutableList() }.toMutableList()

        val listOfPossiblePositionsToShip = possiblePositionsToShip(
            shipSize, battleFieldList
        )
        val randomSet = listOfPossiblePositionsToShip.shuffled().first()

        for (i in 0 until shipSize) {
            battleFieldList[randomSet[i].row][randomSet[i].column].isEnabled = false
            battleFieldList[randomSet[i].row][randomSet[i].column].isShip = true
        }

        // isEnabled=false around ship
        battleFieldList = disableCellsAroundShip(randomSet, battleFieldList)

        _uiState.value = uiState.value.copy(battleFieldList = battleFieldList,
            battleFieldListEnum = battleFieldList.map {
                it.map { it2 ->
                    if (it2.isShip) {
                        CellPrepare.SHIP_ALIVE

                    } else {
                        CellPrepare.EMPTY
                    }
                }
            },
            shipsList = uiState.value.shipsList.mapIndexed() { index, it ->
                if (index == deck) {
                    it.mapIndexed() { ind2, it2 ->
                        if (ind2 == numberOfShip) {
                            ShipsDataClass(isSelected = false, shipCells = randomSet)
                        } else {
                            it2
                        }
                    }
                } else {
                    it
                }

            })

    }

    private fun disableCellsAroundShip(
        randomSet: MutableList<CellPosition>,
        battleFieldList: MutableList<MutableList<CellStatePrepare>>
    ): MutableList<MutableList<CellStatePrepare>> {

        val battleFieldListNew = battleFieldList

        for (i in randomSet.indices) {

            //up
            if (randomSet[i].row - 1 in 0 until 10) {
                battleFieldListNew[randomSet[i].row - 1][randomSet[i].column].isEnabled = false
            }

            //down
            if (randomSet[i].row + 1 in 0 until 10) {
                battleFieldListNew[randomSet[i].row + 1][randomSet[i].column].isEnabled = false
            }

            //left
            if (randomSet[i].column - 1 in 0 until 10) {
                battleFieldListNew[randomSet[i].row][randomSet[i].column - 1].isEnabled = false
            }

            //right
            if (randomSet[i].column + 1 in 0 until 10) {
                battleFieldListNew[randomSet[i].row][randomSet[i].column + 1].isEnabled = false
            }

            //up-left
            if (randomSet[i].row - 1 in 0 until 10 && randomSet[i].column - 1 in 0 until 10) {
                battleFieldListNew[randomSet[i].row - 1][randomSet[i].column - 1].isEnabled = false
            }

            //up-right
            if (randomSet[i].row - 1 in 0 until 10 && randomSet[i].column + 1 in 0 until 10) {
                battleFieldListNew[randomSet[i].row - 1][randomSet[i].column + 1].isEnabled = false
            }

            //down-left
            if (randomSet[i].row + 1 in 0 until 10 && randomSet[i].column - 1 in 0 until 10) {
                battleFieldListNew[randomSet[i].row + 1][randomSet[i].column - 1].isEnabled = false
            }

            //down-right
            if (randomSet[i].row + 1 in 0 until 10 && randomSet[i].column + 1 in 0 until 10) {
                battleFieldListNew[randomSet[i].row + 1][randomSet[i].column + 1].isEnabled = false
            }

        }


        return battleFieldListNew
    }


    //return all possible possitions to ship of one size
    private fun possiblePositionsToShip(
        size: Int, currentBattlefieldList: MutableList<MutableList<CellStatePrepare>>
    ): MutableList<MutableList<CellPosition>> {

        val listOfPossiblePositionsToShip = mutableListOf<MutableList<CellPosition>>()

        //vertical search


        for (column in 0 until 10) {
            for (row in 0 until 10 - size) {


                var isSuitable = true
                val suitableList = mutableListOf<CellPosition>()

                for (i in 0 until size) {
                    if (!currentBattlefieldList[row + i][column].isEnabled) {
                        isSuitable = false
                    } else {
                        suitableList.add(CellPosition(row + i, column))
                    }
                }

                if (isSuitable) {
                    listOfPossiblePositionsToShip.add(suitableList)
                }

            }
        }


        //horizontal search

        for (row in 0 until 10) {
            for (column in 0 until 10 - size) {

                var isSuitable = true
                val suitableList = mutableListOf<CellPosition>()

                for (i in 0 until size) {
                    if (!currentBattlefieldList[row][column + i].isEnabled) {
                        isSuitable = false
                    } else {
                        suitableList.add(CellPosition(row, column + i))
                    }
                }

                if (isSuitable) {
                    listOfPossiblePositionsToShip.add(suitableList)
                }

            }
        }

        return listOfPossiblePositionsToShip
    }


    private fun readyToBattle(): Boolean {

        val listOfAllShipsCells = mutableListOf<CellPosition>()

        uiState.value.shipsList.forEach {
            it.forEach { it2 ->
                it2.shipCells.forEach { it3 ->
                    listOfAllShipsCells.add(it3)
                }
            }
        }

        if (listOfAllShipsCells.size != 20) return false

        val listOfShipSurroundingsCells = mutableListOf<CellPosition>()

        uiState.value.shipsList.forEach {
            it.forEach { it2 ->
                it2.shipCells.forEachIndexed() { i, it3 ->
                    //up
                    if (it2.shipCells[i].row - 1 in 0 until 10) {
                        listOfShipSurroundingsCells.add(
                            CellPosition(
                                it2.shipCells[i].row - 1, it2.shipCells[i].column
                            )
                        )
                    }

                    //down
                    if (it2.shipCells[i].row + 1 in 0 until 10) {
                        listOfShipSurroundingsCells.add(
                            CellPosition(
                                it2.shipCells[i].row + 1, it2.shipCells[i].column
                            )
                        )
                    }

                    //left
                    if (it2.shipCells[i].column - 1 in 0 until 10) {
                        listOfShipSurroundingsCells.add(
                            CellPosition(
                                it2.shipCells[i].row, it2.shipCells[i].column - 1
                            )
                        )
                    }

                    //right
                    if (it2.shipCells[i].column + 1 in 0 until 10) {
                        listOfShipSurroundingsCells.add(
                            CellPosition(
                                it2.shipCells[i].row, it2.shipCells[i].column + 1
                            )
                        )
                    }

                    //up-left
                    if (it2.shipCells[i].row - 1 in 0 until 10 && it2.shipCells[i].column - 1 in 0 until 10) {
                        listOfShipSurroundingsCells.add(
                            CellPosition(
                                it2.shipCells[i].row - 1, it2.shipCells[i].column - 1
                            )
                        )
                    }

                    //up-right
                    if (it2.shipCells[i].row - 1 in 0 until 10 && it2.shipCells[i].column + 1 in 0 until 10) {
                        listOfShipSurroundingsCells.add(
                            CellPosition(
                                it2.shipCells[i].row - 1, it2.shipCells[i].column + 1
                            )
                        )
                    }

                    //down-left
                    if (it2.shipCells[i].row + 1 in 0 until 10 && it2.shipCells[i].column - 1 in 0 until 10) {
                        listOfShipSurroundingsCells.add(
                            CellPosition(
                                it2.shipCells[i].row + 1, it2.shipCells[i].column - 1
                            )
                        )
                    }

                    //down-right
                    if (it2.shipCells[i].row + 1 in 0 until 10 && it2.shipCells[i].column + 1 in 0 until 10) {
                        listOfShipSurroundingsCells.add(
                            CellPosition(
                                it2.shipCells[i].row + 1, it2.shipCells[i].column + 1
                            )
                        )
                    }

                }
            }
        }


//        println(listOfShipSurroundingsCells.size)
//
//        println(listOfShipSurroundingsCells.toSet())

        println(listOfShipSurroundingsCells.toSet().size)


        println(listOfShipSurroundingsCells.toSet().filter {
            it !in listOfAllShipsCells
        }.size)


        var counter = 0
        var dots = 0
        var ships = 0
        var empty = 0
        var conflict = 0


//        println("dots: $dots")
//        println("ships: $ships")
//        println("empty: $empty")
//        println("conflict: $conflict")


        println("do")

        var listDots = uiState.value.battleFieldListEnum

        for (i in 0 until 10) {
            for (j in listDots.indices) {
                if (listDots[i][j] == CellPrepare.DOT) dots += 1
            }
        }

        println("dots: $dots")

        var listShipAlive = uiState.value.battleFieldListEnum

        for (i in 0 until 10) {
            for (j in listShipAlive.indices) {
                if (listShipAlive[i][j] == CellPrepare.SHIP_ALIVE) ships += 1
            }
        }

        println("ships: $ships")

        var listEmpty = uiState.value.battleFieldListEnum

        for (i in 0 until 10) {
            for (j in listEmpty.indices) {
                if (listEmpty[i][j] == CellPrepare.EMPTY) empty += 1
            }
        }

        println("empty: ${empty}")

        println("counter: $counter")


//
//        listOfShipSurroundingsCells.toSet().filter {
//            it !in listOfAllShipsCells
//        }.forEachIndexed() { index, cellPosition ->
//
//            _uiState.value = uiState.value.copy(
//                battleFieldListEnum = uiState.value.battleFieldListEnum.mapIndexed() { ind, it ->
//                    it.mapIndexed { ind2, it2 ->
//                        counter += 1
//                        if (ind == cellPosition.row && ind2 == cellPosition.column) {
//                            Cell.DOT
//                        } else {
//                            it2
//                        }
//                    }
//                }
//            )
//        }


        val filteredCells = listOfShipSurroundingsCells.toSet() - listOfAllShipsCells

        val updatedBattleField = uiState.value.battleFieldListEnum.mapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, cell ->
                counter += 1
                val cellPosition = CellPosition(rowIndex, columnIndex)
                if (cellPosition in filteredCells) {
                    if (cell == CellPrepare.SHIP_ALIVE) conflict += 1
                    dots += 1
                    CellPrepare.DOT
                } else {
                    if (cell == CellPrepare.SHIP_ALIVE) ships += 1 else empty += 1
                    cell
                }
            }
        }

        _uiState.value = uiState.value.copy(battleFieldListEnum = updatedBattleField)


        println("after")
        println("counter: ${counter}")

        dots = 0
        ships = 0
        empty = 0

        listDots = uiState.value.battleFieldListEnum

        for (i in 0 until 10) {
            for (j in listDots.indices) {
                if (listDots[i][j] == CellPrepare.DOT) dots += 1
            }
        }

        println("dots: $dots")

        listShipAlive = uiState.value.battleFieldListEnum

        for (i in 0 until 10) {
            for (j in listShipAlive.indices) {
                if (listShipAlive[i][j] == CellPrepare.SHIP_ALIVE) ships += 1
            }
        }

        println("ships: $ships")

        listEmpty = uiState.value.battleFieldListEnum

        for (i in 0 until 10) {
            for (j in listEmpty.indices) {
                if (listEmpty[i][j] == CellPrepare.EMPTY) empty += 1
            }
        }

        println("empty: ${empty}")
        println("conflict: ${conflict}")


        return conflict == 0

    }

    private fun putDots() {
        println("putDots")
        _uiState.value =
            uiState.value.copy(battleFieldListEnum = uiState.value.battleFieldList.map {
                it.map { it2 ->
                    if (it2.isShip) CellPrepare.SHIP_ALIVE else {
                        if (it2.isEnabled) {
                            CellPrepare.EMPTY
                        } else CellPrepare.DOT
                    }

                }
            })
    }

    private fun roll() {}

    private fun battle() {}
    private fun clickOnCell(row: Int, column: Int) {

        var shipPairIndexes: Pair<Int, Int> = Pair(-1, -1)

        outerLoop@ for (i in uiState.value.shipsList.indices) {
            for (j in uiState.value.shipsList[i].indices) {
                for (k in uiState.value.shipsList[i][j].shipCells.indices) {
                    if (uiState.value.shipsList[i][j].shipCells[k] == CellPosition(row, column)) {
                        shipPairIndexes = Pair(i, j)
                        break@outerLoop
                    }
                }
            }
        }


        val updatedBattleFieldShipsList = uiState.value.shipsList.mapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, cell ->

                if (Pair(rowIndex, columnIndex) == shipPairIndexes) {
                    cell.isSelected = true
                    cell
                } else {
                    cell
                }
            }
        }

        val updatedBattleFieldEnum = uiState.value.battleFieldListEnum.mapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, cell ->

                if (cell == CellPrepare.SHIP_SELECTED){
                    CellPrepare.SHIP_ALIVE
                }else{
                    if (CellPosition(
                            rowIndex, columnIndex
                        ) in uiState.value.shipsList[shipPairIndexes.first][shipPairIndexes.second].shipCells
                    ) {
                        CellPrepare.SHIP_SELECTED
                    }
                    else{
                        cell
                    }
                }

            }
        }

        _uiState.value = uiState.value.copy(
            shipsList = updatedBattleFieldShipsList,
            battleFieldListEnum = updatedBattleFieldEnum
        )

    }

}


data class PrepareUiState(
    val connectionStatus: ConnectionStatus = ConnectionStatus.Nothing,
    val battleFieldListEnum: List<List<CellPrepare>> = listOf(),
    val battleFieldList: List<List<CellStatePrepare>> = listOf(),
    val shipsList: List<List<ShipsDataClass>> = listOf(),
    val isCanGoBattle: Boolean = false
)


sealed class ConnectionStatus() {
    data object Nothing : ConnectionStatus()
    data object Waiting : ConnectionStatus()
    data object Error : ConnectionStatus()
    data object Success : ConnectionStatus()
}


//sealed class PrepareUiState2() {
//    data class field : List<Cell>,
//
//    data object Loading : PrepareUiState2()
//    data class Error(val errorName: String) : PrepareUiState2()
//
//    data class Success(
//        val friendIsConnected: Boolean,
//        val code: String,
//        val battleFieldList: List<List<Cell>>
//    ) : PrepareUiState2()
//
//
//
//}

sealed class PrepareEvent {
    data object UpArrow : PrepareEvent()
    data object LeftArrow : PrepareEvent()
    data object RightArrow : PrepareEvent()
    data object DownArrow : PrepareEvent()
    data object Random : PrepareEvent()
    data object Roll : PrepareEvent()
    data object Battle : PrepareEvent()
    class ClickOnCell(val row: Int, val column: Int) : PrepareEvent()


}

