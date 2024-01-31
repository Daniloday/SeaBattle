package com.missclick.seabattle.presentation.screens.multiplayer

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.missclick.seabattle.common.BaseViewModel
import com.missclick.seabattle.common.Resource
import com.missclick.seabattle.domain.use_cases.ConnectToRoomUseCase
import com.missclick.seabattle.domain.use_cases.ObserveRoomUseCase
import com.missclick.seabattle.presentation.navigation.NavigationKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MultiplayerViewModel @Inject constructor(
    private val connectToRoom: ConnectToRoomUseCase
) :
    BaseViewModel<MultiplayerUiState, MultiplayerEvent>(MultiplayerUiState()){


    override fun obtainEvent(event: MultiplayerEvent) {
        when(event){
            is MultiplayerEvent.Connect -> {
                connect()
            }
            is MultiplayerEvent.OnCodeChange -> {
                onCodeChange(event.newCode)
            }
        }
    }

    private fun connect(){
        if (uiState.value.code.length == 5){
            viewModelScope.launch {
                connectToRoom(code = uiState.value.code, isOwner = false).collect{
                    _uiState.value = uiState.value.copy(
                        connectionStatus = when(it){
                            is Resource.Loading -> ConnectionStatus.Loading
                            is Resource.Error -> {
                                ConnectionStatus.Error.also {
                                    cancel()
                                }
                                
                            }
                            is Resource.Success -> {
                                ConnectionStatus.Success.also {
                                    cancel()
                                }
                            }
                        }
                    )

                }
            }
        }else{
            _uiState.value = uiState.value.copy(
                connectionStatus = ConnectionStatus.Error
            )
        }
    }

    private fun onCodeChange(newCode: String) {
        _uiState.value = uiState.value.copy(
            code = newCode
        )
    }


}

data class MultiplayerUiState(
    val code : String = "",
    val connectionStatus : ConnectionStatus = ConnectionStatus.Nothing
)

sealed class ConnectionStatus(){
    data object Nothing : ConnectionStatus()
    data object Loading : ConnectionStatus()
    data object Error : ConnectionStatus()
    data object Success : ConnectionStatus()
}

sealed class MultiplayerEvent{
    data object Connect: MultiplayerEvent()
    data class OnCodeChange(val newCode : String) : MultiplayerEvent()
}