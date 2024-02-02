package com.missclick.seabattle.presentation.screens.settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.missclick.seabattle.common.BaseViewModel
import com.missclick.seabattle.common.EventHandler
import com.missclick.seabattle.domain.SettingsRepository
import com.missclick.seabattle.domain.use_cases.ObserveSettingsUseCase
import com.missclick.seabattle.domain.use_cases.ReadyUseCase
import com.missclick.seabattle.presentation.screens.prepare.PrepareEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel(), EventHandler<SettingsEvent> {

    val uiState: StateFlow<SettingsUiState> = settingsRepository.settings.map {
        SettingsUiState(
            vibrationOn = it.vibration,
            soundOn = it.volume
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = SettingsUiState(),
        started = SharingStarted.WhileSubscribed(5_000))


    override fun obtainEvent(event: SettingsEvent) {
        when(event){
            is SettingsEvent.ChangeSoundState -> {
                changeSoundState()
            }
            is SettingsEvent.ChangeVibrationState -> {
                changeVibrateState()
            }

        }
    }

    private fun changeSoundState(){
        viewModelScope.launch {
            settingsRepository.updateVolume()
        }
    }

    private fun changeVibrateState(){
        viewModelScope.launch {
            settingsRepository.updateVibration()
        }
    }

}

data class SettingsUiState(
    val soundOn: Boolean = true,
    val vibrationOn: Boolean = true,
)

sealed interface SettingsEvent {
    data object ChangeSoundState : SettingsEvent
    data object ChangeVibrationState : SettingsEvent


}