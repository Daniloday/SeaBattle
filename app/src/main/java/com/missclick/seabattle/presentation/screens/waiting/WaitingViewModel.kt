package com.missclick.seabattle.presentation.screens.waiting

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.missclick.seabattle.common.BaseViewModel
import com.missclick.seabattle.common.Resource
import com.missclick.seabattle.domain.use_cases.CreateRoomUseCase
import com.missclick.seabattle.domain.use_cases.ObserveRoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaitingViewModel @Inject constructor(
    createRoom: CreateRoomUseCase,
    observeRoom: ObserveRoomUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<WaitingUiState, WaitingEvent>( WaitingUiState.Loading) {




    init {
        viewModelScope.launch {
            createRoom().collect { codeResource ->
                when (codeResource) {
                    is Resource.Loading -> {
                        _uiState.value = WaitingUiState.Loading
                    }

                    is Resource.Error -> {
                        _uiState.value = WaitingUiState.Error(codeResource.exception)
                    }

                    is Resource.Success -> {
//                savedStateHandle[NavigationKeys.IS_OWNER] = true
//                savedStateHandle[NavigationKeys.CODE] = it.data.code
                        _uiState.value = WaitingUiState.Success(
                            friendIsConnected = false,
                            code = codeResource.data
                        )
                        observeRoom.invoke(codeResource.data, true).collect { gameResource ->
                            when (gameResource) {
                                is Resource.Loading -> {
                                    _uiState.value = WaitingUiState.Loading
                                }

                                is Resource.Error -> {
                                    _uiState.value = WaitingUiState.Error(gameResource.exception)
                                }
                                is Resource.Success -> {
                                    _uiState.value = WaitingUiState.Success(
                                        friendIsConnected = gameResource.data.friendIsConnected,
                                        code = codeResource.data
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }

    }

    override fun obtainEvent(event: WaitingEvent) {

    }


}

sealed class WaitingUiState() {
    data object Loading : WaitingUiState()
    data class Error(val errorName: String) : WaitingUiState()

    data class Success(
        val friendIsConnected: Boolean,
        val code: String
    ) : WaitingUiState()


}

sealed class WaitingEvent(){

}

