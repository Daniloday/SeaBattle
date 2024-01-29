package com.missclick.seabattle.domain.use_cases

import com.missclick.seabattle.common.asResult
import com.missclick.seabattle.domain.SettingsRepository
import javax.inject.Inject

class ObserveSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke() = settingsRepository.settings.asResult()

}