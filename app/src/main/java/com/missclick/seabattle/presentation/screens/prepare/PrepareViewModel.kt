package com.missclick.seabattle.presentation.screens.prepare

import com.missclick.seabattle.common.BaseViewModel
import com.missclick.seabattle.domain.model.Cell
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

    private fun upArrow() {}
    private fun downArrow() {}
    private fun leftArrow() {}
    private fun rightArrow() {}

    private fun isPossibleToMove(
        direction: String,
        battleFieldList: MutableList<MutableList<CellStatePrepare>>,
        listCellsToMove: MutableList<PossiblePositions>
    ): Boolean {

        when (direction) {
            "up" -> {
                listCellsToMove.forEach { it ->
                    if (battleFieldList[it.row - 1][it.column].isEnabled) return true
                }
            }

            "down" -> {
                listCellsToMove.forEach { it ->
                    if (battleFieldList[it.row + 1][it.column].isEnabled) return true
                }
            }

            "left" -> {
                listCellsToMove.forEach { it ->
                    if (battleFieldList[it.row][it.column - 1].isEnabled) return true
                }
            }

            "right" -> {
                listCellsToMove.forEach { it ->
                    if (battleFieldList[it.row][it.column + 1].isEnabled) return true
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

        println(battleFieldList)

        //start to fill cells

        //add 4-cells ship
        val shipSize = 4
        val listOfPossiblePositionsToShip = possiblePositionsToShip(shipSize, battleFieldList)
        val randomSet = listOfPossiblePositionsToShip.shuffled().first()

        for (i in 0 until shipSize) {
            battleFieldList[randomSet[i].row][randomSet[i].column].isEnabled = false
            battleFieldList[randomSet[i].row][randomSet[i].column].isShip = true
        }
        // isEnabled=false around ship

        battleFieldList = disableCellsAroundShip(randomSet, battleFieldList)
        println(battleFieldList)


        //checker that all ships on field

    }

    private fun disableCellsAroundShip(
        randomSet: MutableList<PossiblePositions>,
        battleFieldList: MutableList<MutableList<CellStatePrepare>>
    ): MutableList<MutableList<CellStatePrepare>> {

        val battleFieldListNew = battleFieldList


        _uiState.value = uiState.value.copy(
            battleFieldList = uiState.value.battleFieldList.map {
               it.map { it2 ->
                   it2
               }
            }
        )

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
        size: Int,
        currentBattlefieldList: MutableList<MutableList<CellStatePrepare>>
    ): MutableList<MutableList<PossiblePositions>> {

        val listOfPossiblePositionsToShip = mutableListOf<MutableList<PossiblePositions>>()

        //vertical search


        for (column in 0 until 10) {
            for (row in 0 until 10 - size) {


                var isSuitable = true
                val suitableList = mutableListOf<PossiblePositions>()

                for (i in 0 until size) {
                    if (!currentBattlefieldList[row + i][column].isEnabled) {
                        isSuitable = false
                    } else {
                        suitableList.add(PossiblePositions(row, column))
                    }
                }

                if (isSuitable) {
                    listOfPossiblePositionsToShip.add(suitableList)

                    println(listOfPossiblePositionsToShip)
                }

            }
        }


        //horizontal search

        for (column in 0 until 10 - size) {
            for (row in 0 until 10) {

                var isSuitable = true
                val suitableList = mutableListOf<PossiblePositions>()

                for (i in 0 until size) {
                    if (!currentBattlefieldList[row][column + i].isEnabled) {
                        isSuitable = false
                    } else {
                        suitableList.add(PossiblePositions(row, column))
                    }
                }

                if (isSuitable) {
                    listOfPossiblePositionsToShip.add(suitableList)

                    println(listOfPossiblePositionsToShip)
                }

            }
        }

        return listOfPossiblePositionsToShip
    }

    private fun roll() {}

    private fun battle() {}
    private fun clickOnCell(row: Int, column: Int) {}

}

data class PossiblePositions(
    var row: Int,
    var column: Int
)


data class PrepareUiState(
    val connectionStatus : ConnectionStatus = ConnectionStatus.Nothing,
    val battleFieldList: List<List<Cell>> = listOf(),
)




sealed class ConnectionStatus(){
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

data class CellStatePrepare(
    var isEnabled: Boolean,
    var isShip: Boolean
)

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

