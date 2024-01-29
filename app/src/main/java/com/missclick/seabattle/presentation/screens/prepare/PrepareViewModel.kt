package com.missclick.seabattle.presentation.screens.prepare

import androidx.lifecycle.SavedStateHandle
import com.missclick.seabattle.common.BaseViewModel
import com.missclick.seabattle.domain.model.Cell
import com.missclick.seabattle.domain.model.CellPrepare
import com.missclick.seabattle.domain.use_cases.ReadyUseCase
import com.missclick.seabattle.presentation.navigation.NavigationKeys
import com.missclick.seabattle.presentation.screens.prepare.models.CellPosition
import com.missclick.seabattle.presentation.screens.prepare.models.CellStatePrepare
import com.missclick.seabattle.presentation.screens.prepare.models.ShipsDataClass
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrepareViewModel @Inject constructor(
    private val ready: ReadyUseCase, savedStateHandle: SavedStateHandle
) : BaseViewModel<PrepareUiState, PrepareEvent>(PrepareUiState()) {

    private val isOwner: Boolean =
        savedStateHandle.get<String>(NavigationKeys.IS_OWNER).toString().toBoolean()
    private val code: String = savedStateHandle[NavigationKeys.CODE]!!
    override fun obtainEvent(event: PrepareEvent) {
        when (event) {
            is PrepareEvent.UpArrow -> {
                directArrow("up")
            }

            is PrepareEvent.DownArrow -> {
                directArrow("down")
            }

            is PrepareEvent.LeftArrow -> {
                directArrow("left")
            }

            is PrepareEvent.RightArrow -> {
                directArrow("right")
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
        _uiState.value = uiState.value.copy(
            code = code, isOwner = isOwner
        )
        random()
    }

    private fun directArrow(direction: String) {
        var ship: MutableList<CellPosition> = mutableListOf()
        var oldShip: MutableList<CellPosition> = mutableListOf()

        uiState.value.shipsList.forEach {
            it.forEach { it2 ->
                if (it2.isSelected) {
                    oldShip = it2.shipCells.toMutableList()
                }
            }
        }

        if (isPossibleToMove(direction, oldShip)) {

            ship = oldShip.map {
                when (direction) {
                    "up" -> {
                        it.row = it.row - 1
                    }

                    "down" -> {
                        it.row = it.row + 1
                    }

                    "left" -> {
                        it.column = it.column - 1
                    }

                    "right" -> {
                        it.column = it.column + 1
                    }
                }
                it
            }.toMutableList()

            _uiState.value = uiState.value.copy(
                shipsList = uiState.value.shipsList.map {
                    it.map { it2 ->
                        if (it2.isSelected) {
                            it2.shipCells = ship.toList()
                            it2
                        } else {
                            it2
                        }
                    }
                    it
                },
            )


            make_battleFieldList_from_shipList()

            make_battleFieldListEnum_from_battleFieldList()

            colorSelectedShip()

            _uiState.value = uiState.value.copy(isCanGoBattle = readyToBattle())
        }
    }

    private fun colorSelectedShip() {


        var ship: MutableList<CellPosition> = mutableListOf()

        uiState.value.shipsList.forEach {
            it.forEach { it2 ->
                if (it2.isSelected) {
                    ship = it2.shipCells.toMutableList()
                }
            }
        }


        _uiState.value = uiState.value.copy(
            battleFieldListEnum = uiState.value.battleFieldListEnum.mapIndexed { rowIndex, row ->
                row.mapIndexed { columnIndex, cell ->

                    if (CellPosition(
                            rowIndex, columnIndex
                        ) in ship
                    ) {
                        CellPrepare.SHIP_SELECTED
                    } else {
                        cell
                    }


                }
            }
        )


    }


    private fun make_battleFieldList_from_shipList() {
        _uiState.value = uiState.value.copy(
            battleFieldList = uiState.value.battleFieldList.mapIndexed() { index, cellStatePrepares ->
                cellStatePrepares.mapIndexed { index2, cellStatePrepare2 ->
                    cellStatePrepare2.isEnabled = true
                    cellStatePrepare2.isShip = false


                    uiState.value.shipsList.forEach {
                        it.forEach { it2 ->

                            for (i in it2.shipCells) {
                                if (CellPosition(index, index2) == i) {
                                    cellStatePrepare2.isEnabled = false
                                    cellStatePrepare2.isShip = true
                                }
                            }

                        }
                    }
                    cellStatePrepare2

                }
            }
        )

        uiState.value.shipsList.forEach {
            it.forEach { it2 ->
                disableCellsAroundShip(
                    it2.shipCells.toMutableList(),
                    uiState.value.battleFieldList.map { it3 -> it3.toMutableList() }.toMutableList()
                )

            }
        }
    }

    private fun make_battleFieldListEnum_from_battleFieldList() {

        _uiState.value = uiState.value.copy(
            battleFieldListEnum = uiState.value.battleFieldList.map {
                it.map { it2 ->
                    if (it2.isShip) {
                        CellPrepare.SHIP_ALIVE

                    } else {
                        CellPrepare.EMPTY
                    }
                }
            }
        )

    }


    fun isPossibleToMove(
        direction: String, listCellsToMove: MutableList<CellPosition>
    ): Boolean {

        var returnBool = true

        when (direction) {
            "up" -> {
                listCellsToMove.forEach { it ->
                    if (it.row - 1 !in 0 until 10) returnBool = false

                }
            }

            "down" -> {
                listCellsToMove.forEach { it ->
                    if (it.row + 1 !in 0 until 10) returnBool = false
                }
            }

            "left" -> {
                listCellsToMove.forEach { it ->
                    if (it.column - 1 !in 0 until 10) returnBool = false
                }
            }

            "right" -> {
                listCellsToMove.forEach { it ->
                    if (it.column + 1 !in 0 until 10) returnBool = false
                }
            }
        }

        return returnBool
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

        _uiState.value = uiState.value.copy(
            isCanGoBattle = readyToBattle()
        )

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

        var listOfAllShipsCells = mutableListOf<CellPosition>()

        uiState.value.shipsList.forEach {
            it.forEach { it2 ->
                it2.shipCells.forEach { it3 ->
                    listOfAllShipsCells.add(it3)
                }
            }
        }

        listOfAllShipsCells = listOfAllShipsCells.toSet().toMutableList()

        if (listOfAllShipsCells.size != 20) {
            println("<20")
            return false
        }

        var conflict = 0
        var counter = 0

        var listOfShipSurroundingsCells: MutableList<CellPosition> = mutableListOf()

        uiState.value.shipsList.forEach { it ->
            it.forEach { it2 ->
                disableCellsAroundOneShip(it2.shipCells.toMutableList()).forEach { it3 ->
                    listOfShipSurroundingsCells.add(it3)
                }
            }
        }

        listOfShipSurroundingsCells = listOfShipSurroundingsCells.toSet().toMutableList()

        listOfAllShipsCells.forEach { shipCell ->
            listOfShipSurroundingsCells.forEach { surroundCell ->
                counter += 1
                if (surroundCell == shipCell) {
                    conflict += 1
                }
            }
        }

        return conflict == 0

    }

    private fun disableCellsAroundOneShip(
        shipSet: MutableList<CellPosition>
    ): MutableList<CellPosition> {

        var listOfDisableCellsAroundOneShip = mutableListOf<CellPosition>()

        for (i in shipSet.indices) {

            //up
            if (shipSet[i].row - 1 in 0 until 10) {
                listOfDisableCellsAroundOneShip.add(
                    CellPosition(
                        shipSet[i].row - 1,
                        shipSet[i].column
                    )
                )
            }

            //down
            if (shipSet[i].row + 1 in 0 until 10) {
                listOfDisableCellsAroundOneShip.add(
                    CellPosition(
                        shipSet[i].row + 1,
                        shipSet[i].column
                    )
                )
            }

            //left
            if (shipSet[i].column - 1 in 0 until 10) {
                listOfDisableCellsAroundOneShip.add(
                    CellPosition(
                        shipSet[i].row,
                        shipSet[i].column - 1
                    )
                )
            }

            //right
            if (shipSet[i].column + 1 in 0 until 10) {
                listOfDisableCellsAroundOneShip.add(
                    CellPosition(
                        shipSet[i].row,
                        shipSet[i].column + 1
                    )
                )
            }

            //up-left
            if (shipSet[i].row - 1 in 0 until 10 && shipSet[i].column - 1 in 0 until 10) {
                listOfDisableCellsAroundOneShip.add(
                    CellPosition(
                        shipSet[i].row - 1,
                        shipSet[i].column - 1
                    )
                )
            }

            //up-right
            if (shipSet[i].row - 1 in 0 until 10 && shipSet[i].column + 1 in 0 until 10) {
                listOfDisableCellsAroundOneShip.add(
                    CellPosition(
                        shipSet[i].row - 1,
                        shipSet[i].column + 1
                    )
                )
            }

            //down-left
            if (shipSet[i].row + 1 in 0 until 10 && shipSet[i].column - 1 in 0 until 10) {
                listOfDisableCellsAroundOneShip.add(
                    CellPosition(
                        shipSet[i].row + 1,
                        shipSet[i].column - 1
                    )
                )
            }

            //down-right
            if (shipSet[i].row + 1 in 0 until 10 && shipSet[i].column + 1 in 0 until 10) {
                listOfDisableCellsAroundOneShip.add(
                    CellPosition(
                        shipSet[i].row + 1,
                        shipSet[i].column + 1
                    )
                )
            }

        }

        listOfDisableCellsAroundOneShip = (listOfDisableCellsAroundOneShip.toSet()
            .toMutableList() - shipSet.toMutableList()).toMutableList()


        return listOfDisableCellsAroundOneShip
    }

    private fun roll() {

        var shipSet: MutableList<CellPosition> = mutableListOf()

        uiState.value.shipsList.forEach {
            it.forEach { it2 ->
                if (it2.isSelected) {
                    shipSet = it2.shipCells.toMutableList()
                }
            }
        }

        shipSet.map {
            val column = it.row
            it.row = it.column
            it.column = column
            it
        }

        println(shipSet)

        uiState.value.shipsList.map {
            it.map { it2 ->
                if (it2.isSelected) {
                    it2.shipCells = shipSet
                }
            }
        }

        make_battleFieldList_from_shipList()
        make_battleFieldListEnum_from_battleFieldList()
        colorSelectedShip()

        _uiState.value = uiState.value.copy(isCanGoBattle = readyToBattle())

    }

    private fun battle() {
        ready.invoke(
            cells = uiState.value.battleFieldListEnum.map { row ->
                row.map {
                    when (it) {
                        CellPrepare.SHIP_ALIVE, CellPrepare.SHIP_SELECTED -> Cell.SHIP_ALIVE
                        else -> Cell.EMPTY
                    }
                }
            }, isOwner = isOwner, code = code
        )
    }

    private fun clickOnCell(row: Int, column: Int) {

        var oldShip: MutableList<CellPosition> = mutableListOf()


        uiState.value.shipsList.forEach {
            it.forEach { it2 ->
                if (it2.isSelected) {
                    oldShip = it2.shipCells.toMutableList()
                }
            }
        }

        var shipPairIndexes: Pair<Int, Int> = Pair(-1, -1)

        if (!oldShip.contains(CellPosition(row, column)) && oldShip.isNotEmpty()) {
            outerLoop@ for (i in uiState.value.shipsList.indices) {
                for (j in uiState.value.shipsList[i].indices) {
                    for (k in uiState.value.shipsList[i][j].shipCells.indices) {
                        if (uiState.value.shipsList[i][j].shipCells[k] == CellPosition(
                                oldShip[0].row,
                                oldShip[0].column
                            )
                        ) {
                            shipPairIndexes = Pair(i, j)
                            break@outerLoop
                        }
                    }
                }
            }

            _uiState.value = uiState.value.copy(
                shipsList = uiState.value.shipsList.mapIndexed() { index, shipsDataClasses ->
                    shipsDataClasses.mapIndexed { index2, shipsDataClass2 ->
                        if (shipPairIndexes == Pair(index, index2)) {
                            shipsDataClass2.isSelected = false
                            shipsDataClass2
                        } else {
                            shipsDataClass2
                        }
                    }
                }
            )

            make_battleFieldList_from_shipList()
            make_battleFieldListEnum_from_battleFieldList()
        }

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
                if (CellPosition(
                        rowIndex, columnIndex
                    ) in uiState.value.shipsList[shipPairIndexes.first][shipPairIndexes.second].shipCells
                ) {
                    CellPrepare.SHIP_SELECTED
                } else {
                    cell
                }


            }
        }

        _uiState.value = uiState.value.copy(
            shipsList = updatedBattleFieldShipsList, battleFieldListEnum = updatedBattleFieldEnum
        )

    }

}


data class PrepareUiState(
    val connectionStatus: ConnectionStatus = ConnectionStatus.Nothing,
    val battleFieldListEnum: List<List<CellPrepare>> = listOf(),
    val battleFieldList: List<List<CellStatePrepare>> = listOf(),
    val shipsList: List<List<ShipsDataClass>> = listOf(),
    val isCanGoBattle: Boolean = false,
    val code: String = "",
    val isOwner: Boolean = true
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

