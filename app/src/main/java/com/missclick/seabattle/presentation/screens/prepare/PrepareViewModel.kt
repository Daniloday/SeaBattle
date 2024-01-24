package com.missclick.seabattle.presentation.screens.prepare

import com.missclick.seabattle.common.BaseViewModel
import com.missclick.seabattle.presentation.screens.multiplayer.MultiplayerEvent
import com.missclick.seabattle.presentation.screens.multiplayer.MultiplayerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PrepareViewModel @Inject constructor() :
    BaseViewModel<PrepareUiState, PrepareEvent>(PrepareUiState("default"))
{


    override fun obtainEvent(event: PrepareEvent) {
        when(event){
            is PrepareEvent.Next -> {

            }
        }
    }


}

data class PrepareUiState(
    val a : String
)

sealed class PrepareEvent{
    object Next : PrepareEvent()
}