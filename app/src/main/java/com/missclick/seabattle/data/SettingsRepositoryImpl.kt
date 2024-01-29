package com.missclick.seabattle.data

import androidx.datastore.core.DataStore
import com.missclick.seabattle.domain.SettingsRepository
import com.missclick.seabattle.domain.model.Settings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsDataStore : DataStore<Settings>
) : SettingsRepository {

    override val settings: Flow<Settings> = settingsDataStore.data

    override suspend fun updateVibration() {
        settingsDataStore.updateData {
            it.copy(
                vibration = !it.vibration
            )
        }
    }

    override suspend fun updateVolume() {
        settingsDataStore.updateData {
            it.copy(
                volume = !it.volume
            )
        }
    }

}