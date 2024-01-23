package com.missclick.seabattle.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<S,E>(uiState : S) : ViewModel(), EventHandler<E>{


    protected val _uiState: MutableStateFlow<S> = MutableStateFlow(uiState)
    val uiState: StateFlow<S> = _uiState


}