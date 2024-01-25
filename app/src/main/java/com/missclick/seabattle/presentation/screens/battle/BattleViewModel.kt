package com.missclick.seabattle.presentation.screens.battle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.missclick.seabattle.common.EventHandler
import com.missclick.seabattle.common.Resource
import com.missclick.seabattle.domain.model.Cell
import com.missclick.seabattle.domain.model.Game
import com.missclick.seabattle.domain.use_cases.CreateRoomAndObserveUseCase
import com.missclick.seabattle.domain.use_cases.ConnectAndObserveRoomUseCase
import com.missclick.seabattle.domain.use_cases.DoStepUseCase
import com.missclick.seabattle.domain.use_cases.ReadyUseCase
import com.missclick.seabattle.presentation.navigation.NavigationKeys
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
    connectAndObserveRoom: ConnectAndObserveRoomUseCase,
    val doStepUseCase: DoStepUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel(), EventHandler<BattleEvent> {

    val uiState : StateFlow<BattleUiState> = connectAndObserveRoom(
        code = savedStateHandle[NavigationKeys.CODE]!!,
        isOwner = savedStateHandle[NavigationKeys.IS_OWNER]!!,
    ).map {
        when(it){
            is Resource.Loading -> { BattleUiState.Loading}
            is Resource.Error -> { BattleUiState.Error(it.exception)}
            is Resource.Success -> { BattleUiState.Success(
                yourCells = it.data.yourCells,
                friendCells = it.data.friendCells,
                yourMove = it.data.yourMove
            )}
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BattleUiState.Loading
        )

    override fun obtainEvent(event: BattleEvent) {

    }


}

sealed class BattleUiState(){
    data object Loading : BattleUiState()
    data class Error(val errorName : String) : BattleUiState()

    data class Success(val yourCells : List<List<Cell>>,
                       val friendCells : List<List<Cell>>,
                       val yourMove : Boolean = true,) : BattleUiState()

}

sealed class BattleEvent{
    class DoStep(y : Int, x : Int)
}



