package com.missclick.seabattle.presentation.screens.settings

import androidx.lifecycle.SavedStateHandle
import com.missclick.seabattle.common.BaseViewModel
import com.missclick.seabattle.domain.use_cases.ReadyUseCase
import com.missclick.seabattle.presentation.screens.prepare.PrepareEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val ready: ReadyUseCase, savedStateHandle: SavedStateHandle
) : BaseViewModel<SettingsUiState, SettingsEvent>(SettingsUiState()) {

    override fun obtainEvent(event: SettingsEvent) {
        when(event){
            is SettingsEvent.ChangeSoundState -> {

            }
            is SettingsEvent.ChangeVibrationState -> {

            }
            is SettingsEvent.ChangeLanguage -> {

            }
        }
    }

    private fun changeSoundState(){

    }

    private fun changeVibrateState(){

    }

    private fun changeLanguage(){

    }

}

data class SettingsUiState(
    val soundOn: Boolean = true,
    val vibrationOn: Boolean = true,
    val language: String = "UKR"
)

sealed class SettingsEvent {
    data object ChangeSoundState : SettingsEvent()
    data object ChangeVibrationState : SettingsEvent()
    data object ChangeLanguage : SettingsEvent()

}