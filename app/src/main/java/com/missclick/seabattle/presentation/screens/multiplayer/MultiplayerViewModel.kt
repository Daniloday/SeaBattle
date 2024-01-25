package com.missclick.seabattle.presentation.screens.multiplayer

import androidx.lifecycle.SavedStateHandle
import com.missclick.seabattle.common.BaseViewModel
import com.missclick.seabattle.presentation.navigation.NavigationKeys
import com.missclick.seabattle.presentation.screens.menu.MenuEvent
import com.missclick.seabattle.presentation.screens.menu.MenuUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MultiplayerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) :
    BaseViewModel<MultiplayerUiState, MultiplayerEvent>(MultiplayerUiState("default")){


    override fun obtainEvent(event: MultiplayerEvent) {
        when(event){
            is MultiplayerEvent.Connect -> {
                connect(event.code)
            }
        }
    }

    private fun connect(code : String){
        //todo rewrite with state and connection and checker
        savedStateHandle[NavigationKeys.CODE] = code
        savedStateHandle[NavigationKeys.IS_OWNER] = false
    }


}

data class MultiplayerUiState(
    val a : String
)

sealed class MultiplayerEvent{
    data class Connect(val code : String) : MultiplayerEvent()
}