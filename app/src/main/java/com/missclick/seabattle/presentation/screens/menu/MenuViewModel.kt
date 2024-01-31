package com.missclick.seabattle.presentation.screens.menu

import com.missclick.seabattle.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor() :
    BaseViewModel<MenuUiState, MenuEvent>(MenuUiState("default"))
{


    override fun obtainEvent(event: MenuEvent) {
      
        when(event){
            is MenuEvent.Next -> {

            }
        }
    }



}

data class MenuUiState(
    val a : String
)

sealed class MenuEvent{
    object Next : MenuEvent()
}