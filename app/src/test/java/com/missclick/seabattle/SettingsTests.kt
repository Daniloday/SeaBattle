package com.missclick.seabattle

import androidx.test.platform.app.InstrumentationRegistry
import com.missclick.seabattle.common.asResult
import com.missclick.seabattle.domain.SettingsRepository
import org.junit.Test
import javax.inject.Inject
import kotlinx.coroutines.test.runTest
import app.cash.turbine.test
import com.missclick.seabattle.common.Resource
import com.missclick.seabattle.di.AppModule
import com.missclick.seabattle.domain.model.Settings
import com.missclick.seabattle.domain.model.SettingsSerializer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SettingsTest {

    private lateinit var settingsRepository: SettingsRepository

    @Before
    fun init() {
        val instrumentationContext = InstrumentationRegistry.getInstrumentation().context
        settingsRepository = AppModule.provideSettingsRepository(
            AppModule.provideDataStore(
                context = instrumentationContext,
                settingsSerializer = SettingsSerializer())
        )
    }

    @Test
    fun checkDefaults() = runTest{
        settingsRepository.settings.asResult().test {
            assertEquals(Resource.Loading, awaitItem())
            assertEquals(Resource.Success(Settings(vibration = true, volume = true)), awaitItem())
            cancel()
        }

    }


    @Test
    fun checkUpdateVibration() = runTest{
        settingsRepository.updateVibration()
        settingsRepository.settings.asResult().test {
            assertEquals(Resource.Loading, awaitItem())
            assertEquals(Resource.Success(Settings(vibration = false, volume = true)), awaitItem())
            cancel()
        }

    }

    @Test
    fun checkUpdateVolume() = runTest{
        settingsRepository.updateVolume()
        settingsRepository.settings.asResult().test {
            assertEquals(Resource.Loading, awaitItem())
            assertEquals(Resource.Success(Settings(vibration = true, volume = false)), awaitItem())
            cancel()
        }

    }


    @Test
    fun checkUpdateAll() = runTest{
        settingsRepository.updateVibration()
        settingsRepository.updateVolume()
        settingsRepository.settings.asResult().test {
            assertEquals(Resource.Loading, awaitItem())
            assertEquals(Resource.Success(Settings(vibration = false, volume = false)), awaitItem())
            cancel()
        }

    }

    @Test
    fun checkUpdateVibrationTwice() = runTest{
        settingsRepository.updateVibration()
        settingsRepository.updateVibration()
        settingsRepository.settings.asResult().test {
            assertEquals(Resource.Loading, awaitItem())
            assertEquals(Resource.Success(Settings(vibration = true, volume = true)), awaitItem())
            cancel()
        }

    }



}