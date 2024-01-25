package com.missclick.seabattle.presentation.screens.waiting

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.missclick.seabattle.common.Resource
import com.missclick.seabattle.domain.model.Cell
import com.missclick.seabattle.domain.use_cases.CreateRoomAndObserveUseCase
import com.missclick.seabattle.presentation.navigation.NavigationKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WaitingViewModel @Inject constructor(
    createRoomAndObserve: CreateRoomAndObserveUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    val uiState: StateFlow<WaitingUiState> = createRoomAndObserve().map {
        when (it) {
            is Resource.Loading -> {
                WaitingUiState.Loading
            }

            is Resource.Error -> {
                WaitingUiState.Error(it.exception)
            }

            is Resource.Success -> {
                savedStateHandle[NavigationKeys.IS_OWNER] = true
                savedStateHandle[NavigationKeys.CODE] = it.data.code
                WaitingUiState.Success(
                    friendIsConnected = it.data.friendIsConnected,
                    code = it.data.code
                )
            }
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = WaitingUiState.Loading
    )

}

sealed class WaitingUiState() {
    data object Loading : WaitingUiState()
    data class Error(val errorName: String) : WaitingUiState()

    data class Success(
        val friendIsConnected: Boolean,
        val code: String
    ) : WaitingUiState()


}

