package com.missclick.seabattle.presentation.screens.multiplayer

import com.missclick.seabattle.common.BaseViewModel
import com.missclick.seabattle.presentation.screens.menu.MenuEvent
import com.missclick.seabattle.presentation.screens.menu.MenuUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MultiplayerViewModel @Inject constructor() :
    BaseViewModel<MultiplayerUiState, MultiplayerEvent>(MultiplayerUiState("default"))
{


    override fun obtainEvent(event: MultiplayerEvent) {
        when(event){
            is MultiplayerEvent.Next -> {

            }
        }
    }


}

data class MultiplayerUiState(
    val a : String
)

sealed class MultiplayerEvent{
    object Next : MultiplayerEvent()
}