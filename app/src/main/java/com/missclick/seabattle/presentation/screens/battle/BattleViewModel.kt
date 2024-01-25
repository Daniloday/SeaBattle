package com.missclick.seabattle.presentation.screens.battle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.missclick.seabattle.common.Resource
import com.missclick.seabattle.domain.model.Cell
import com.missclick.seabattle.domain.model.Game
import com.missclick.seabattle.domain.use_cases.CreateRoomAndObserveUseCase
import com.missclick.seabattle.domain.use_cases.ConnectAndObserveRoomUseCase
import com.missclick.seabattle.domain.use_cases.DoStepUseCase
import com.missclick.seabattle.domain.use_cases.ReadyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BattleViewModel @Inject constructor(
    createRoom: CreateRoomAndObserveUseCase,
    observeRoom: ConnectAndObserveRoomUseCase,
    val readyUseCase: ReadyUseCase,
    val doStepUseCase: DoStepUseCase
) : ViewModel() {

    val uiState : StateFlow<BattleUiState> = createRoom().map {
        when(it){
            is Resource.Loading -> { BattleUiState.Loading}
            is Resource.Error -> { BattleUiState.Error(it.exception)}
            is Resource.Success -> { it.data.toUiState()}
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BattleUiState.Loading
        )

    init {
        viewModelScope.launch {
            delay(10000)
            setReady1(true)
        }
    }

    suspend fun setReady1(isOwner : Boolean){

        val t = mutableListOf(listOf(Cell.SHIP_ALIVE, Cell.SHIP_ALIVE, Cell.SHIP_DAMAGE, Cell.EMPTY,  Cell.EMPTY , Cell.EMPTY, Cell.EMPTY, Cell.EMPTY, Cell.EMPTY, Cell.EMPTY))
        repeat(9){
            t.add(listOf( Cell.EMPTY, Cell.EMPTY, Cell.EMPTY, Cell.EMPTY, Cell.EMPTY, Cell.EMPTY, Cell.EMPTY, Cell.EMPTY, Cell.EMPTY, Cell.EMPTY))
        }

        if (uiState.value is BattleUiState.Success){
            readyUseCase(code = (uiState.value as BattleUiState.Success).code, isOwner = true, cells = t)
            delay(10000)
            readyUseCase(code = (uiState.value as BattleUiState.Success).code, isOwner = false, cells = t)
            delay(10000)
            doStepUseCase.invoke(0,1, (uiState.value as BattleUiState.Success).friendCells, isOwner = true, code = (uiState.value as BattleUiState.Success).code)
        }

    }

}

sealed class BattleUiState(){
    data object Loading : BattleUiState()
    data class Error(val errorName : String) : BattleUiState()

    data class Success(val yourCells : List<List<Cell>>,
                       val friendCells : List<List<Cell>>,
                       val code : String,
                       val friendIsConnected : Boolean = false,
                       val friendIsReady : Boolean = false,
                       val youAreReady : Boolean = false,
                       val yourMove : Boolean = true,) : BattleUiState()



}

fun Game.toUiState() : BattleUiState.Success {
    return BattleUiState.Success(
        yourCells = yourCells,
        friendCells = friendCells,
        code = code,
        friendIsConnected = friendIsConnected,
        friendIsReady = friendIsReady,
        youAreReady = youAreReady,
        yourMove = yourMove
    )
}

