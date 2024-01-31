package com.missclick.seabattle.presentation.screens.battle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.missclick.seabattle.common.BaseViewModel
import com.missclick.seabattle.common.EventHandler
import com.missclick.seabattle.common.Resource
import com.missclick.seabattle.domain.model.Cell
import com.missclick.seabattle.domain.use_cases.ConnectToRoomUseCase
import com.missclick.seabattle.domain.use_cases.DeleteRoomUseCase
import com.missclick.seabattle.domain.use_cases.ObserveRoomUseCase
import com.missclick.seabattle.domain.use_cases.DoStepUseCase
import com.missclick.seabattle.presentation.navigation.NavigationKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.checkerframework.checker.units.qual.s
import javax.inject.Inject

@HiltViewModel
class BattleViewModel @Inject constructor(
    observeRoom: ObserveRoomUseCase,
    private val doStepUseCase: DoStepUseCase,
    private val connectToRoomUseCase: ConnectToRoomUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val deleteRoomUseCase: DeleteRoomUseCase
) : BaseViewModel<BattleUiState, BattleEvent>(BattleUiState.Loading) {

    val isOwner : Boolean = savedStateHandle.get<String>(NavigationKeys.IS_OWNER).toString().toBoolean()
    val code : String = savedStateHandle.get<String>(NavigationKeys.CODE).toString()


    private val _uiStateExitDialog = MutableStateFlow(false)

    val uiStateExitDialog : StateFlow<Boolean> = _uiStateExitDialog



    init {
        viewModelScope.launch {
            observeRoom(
                code = code,
                isOwner = isOwner,
            ).collect {
                _uiState.value = when (it) {
                    is Resource.Loading -> {
                        BattleUiState.Loading
                    }

                    is Resource.Error -> {
                        BattleUiState.Error(it.exception)
                    }

                    is Resource.Success -> {
                        if ((it.data.friendIsReady && it.data.youAreReady) || it.data.friendIsConnected || it.data.youAreConnected){
                            BattleUiState.Success(
                                yourCells = it.data.yourCells,
                                friendCells = it.data.friendCells,
                                yourMove = it.data.yourMove,
                                youAreWinner = it.data.youAreWinner,
                                friendsIsConnected = it.data.friendIsConnected,
                                youAreConnected = it.data.youAreConnected
                            )
                        }else{
                            BattleUiState.Loading
                        }

                    }
                }
            }
        }
    }

    override fun obtainEvent(event: BattleEvent) {
        when (event) {
            is BattleEvent.DoStep -> {
                doStep(y = event.y, x = event.x)
            }
            is BattleEvent.Restart -> {
                restart()
            }
            is BattleEvent.Exit -> {
                exit()
            }
            is BattleEvent.Back -> {
                _uiStateExitDialog.value = true
            }
            is BattleEvent.CloseDialog -> {
                _uiStateExitDialog.value = false
            }
        }
    }

    private fun exit(){
        deleteRoomUseCase(code)
    }

    private fun restart(){
        println("restart")
        viewModelScope.launch {
            connectToRoomUseCase(code = code, isOwner = isOwner).collect()
        }
    }

    private fun doStep(y: Int, x: Int) {
        val success = uiState.value
        if (success is BattleUiState.Success) {
            if (success.friendCells[y][x] != Cell.SHIP_ALIVE){
                _uiState.value = success.copy(
                    yourMove = false
                )
            }
            doStepUseCase(
                yIndex = y, xIndex = x, friendCells = success.friendCells,
                code = code,
                isOwner = isOwner
            )
        }

    }


}

sealed class BattleUiState {
    data object Loading : BattleUiState()
    data class Error(val errorName: String) : BattleUiState()

    data class Success(
        val yourCells: List<List<Cell>>,
        val friendCells: List<List<Cell>>,
        val yourMove: Boolean = true,
        val youAreWinner : Boolean? = null,
        val friendsIsConnected : Boolean = false,
        val youAreConnected : Boolean = false
    ) : BattleUiState()


}

sealed class BattleEvent {
    class DoStep(val y: Int, val x: Int) : BattleEvent()
    data object Restart : BattleEvent()

    data object Exit : BattleEvent()
    data object Back : BattleEvent()
    data object CloseDialog : BattleEvent()
}



