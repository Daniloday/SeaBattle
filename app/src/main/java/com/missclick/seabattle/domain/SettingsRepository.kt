package com.missclick.seabattle.domain

import com.missclick.seabattle.domain.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val settings : Flow<Settings>

    suspend fun updateVibration()

    suspend fun updateVolume()


}