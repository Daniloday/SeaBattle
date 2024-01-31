package com.missclick.seabattle.presentation.screens.waiting

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.missclick.seabattle.common.BaseViewModel
import com.missclick.seabattle.common.Resource
import com.missclick.seabattle.domain.use_cases.CreateRoomUseCase
import com.missclick.seabattle.domain.use_cases.DeleteRoomUseCase
import com.missclick.seabattle.domain.use_cases.ObserveRoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WaitingViewModel @Inject constructor(
    private val createRoom: CreateRoomUseCase,
    private val observeRoom: ObserveRoomUseCase,
    private val deleteRoom: DeleteRoomUseCase
) : BaseViewModel<WaitingUiState, WaitingEvent>( WaitingUiState.Loading) {


    private var code : String? = null
    private val scope = viewModelScope

    init {
       connect()
    }



    private fun connect(){
        scope.launch {
            createRoom().collect { codeResource ->
                when (codeResource) {
                    is Resource.Loading -> {
                        _uiState.value = WaitingUiState.Loading
                    }

                    is Resource.Error -> {
                        _uiState.value = WaitingUiState.Error(codeResource.exception)
                        cancel()
                    }

                    is Resource.Success -> {
                        code = codeResource.data
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
                                    cancel()
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
        when(event){
            is WaitingEvent.Copy -> {
                copy(event.context)
            }
            is WaitingEvent.Exit -> {
                exit()
            }
            is WaitingEvent.Reconnect -> {
                connect()
            }
        }
    }


    private fun exit(){
        scope.cancel()
        code?.let { deleteRoom(it) }
    }

    private fun copy(context: Context) {
        val state = uiState.value
        if (state is WaitingUiState.Success){
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", state.code)
            clipboard.setPrimaryClip(clip)
        }

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
    data class Copy(val context: Context) : WaitingEvent()
    data object Exit : WaitingEvent()
    data object Reconnect : WaitingEvent()

}

