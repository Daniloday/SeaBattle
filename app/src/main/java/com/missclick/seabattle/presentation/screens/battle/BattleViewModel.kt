package com.missclick.seabattle.presentation.screens.battle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.missclick.seabattle.common.Resource
import com.missclick.seabattle.domain.use_cases.CreateRoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BattleViewModel @Inject constructor(
    createRoom: CreateRoomUseCase
) : ViewModel() {

    val uiState : StateFlow<BattleUiState> = createRoom().map {
        when(it){
            is Resource.Loading -> { BattleUiState.Loading}
            is Resource.Error -> { BattleUiState.Error(it.exception)}
            is Resource.Success -> { BattleUiState.Success(it.data)}
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BattleUiState.Loading
        )

}

sealed class BattleUiState(){
    data object Loading : BattleUiState()
    data class Error(val errorName : String) : BattleUiState()

    data class Success( val field : String) : BattleUiState()

}