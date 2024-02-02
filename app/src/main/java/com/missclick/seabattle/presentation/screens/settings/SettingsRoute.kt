package com.missclick.seabattle.presentation.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.Lottie
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimatable
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.missclick.seabattle.R
import com.missclick.seabattle.presentation.components.BackMark
import com.missclick.seabattle.presentation.components.Connecting
import com.missclick.seabattle.presentation.components.click
import com.missclick.seabattle.presentation.navigation.NavigationTree
import com.missclick.seabattle.presentation.ui.theme.AppTheme

@Composable
fun SettingsRoute(navController: NavController, vm: SettingsViewModel = hiltViewModel()) {

    val uiState by vm.uiState.collectAsState()

    SettingsScreen(uiState = uiState, obtainEvent = vm::obtainEvent, navigateToBack = {
        navController.popBackStack()
    })


}

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    obtainEvent: (SettingsEvent) -> Unit,
    navigateToBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is SettingsUiState.Loading -> {
                Connecting(Modifier.align(Alignment.Center))
            }

            is SettingsUiState.Success -> {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 50.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(id = R.string.sound),
                            style = AppTheme.typography.h1,
                            color = AppTheme.colors.primary
                        )

                        val remSoundOnOff by rememberLottieComposition(
                            spec = LottieCompositionSpec.RawRes(
                                R.raw.anim_sound_on_off
                            )
                        )

                        val animSpecOnStart = LottieClipSpec.Progress(
                            min = 0.5f, max = 1f
                        )

                        val animSpecOffStart = LottieClipSpec.Progress(
                            min = 0f, max = 0.5f
                        )

                        println("soundOn: ${uiState.settings.volume}")
                        if (uiState.settings.volume) {
                            LottieAnimation(
                                modifier = Modifier
                                    .size(100.dp)
                                    .click {
                                        obtainEvent(SettingsEvent.ChangeSoundState)
                                    },
                                composition = remSoundOnOff,
                                clipSpec = animSpecOffStart,
                                maintainOriginalImageBounds = true
                            )

                        } else {
                            LottieAnimation(
                                modifier = Modifier
                                    .size(100.dp)
                                    .click {
                                        obtainEvent(SettingsEvent.ChangeSoundState)
                                    },
                                composition = remSoundOnOff,
                                clipSpec = animSpecOnStart,
                                maintainOriginalImageBounds = true
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(40.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                            .padding(horizontal = 50.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(id = R.string.vibro),
                            style = AppTheme.typography.h1,
                            color = AppTheme.colors.primary
                        )

                        val remVibroOnOff by rememberLottieComposition(
                            spec = LottieCompositionSpec.RawRes(
                                R.raw.anim_vibro_on_off
                            )
                        )


                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clickable(
                                    indication = null,
                                    interactionSource = MutableInteractionSource()
                                ) {
                                    obtainEvent(SettingsEvent.ChangeVibrationState)
                                }, contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.phone_vibrate),
                                contentDescription = "phone vibration",
                                modifier = Modifier.size(30.dp)
                            )

                            if (uiState.settings.vibration) {
                                LottieAnimation(
                                    modifier = Modifier.size(60.dp),
                                    composition = remVibroOnOff,
                                    clipSpec = LottieClipSpec.Progress(
                                        min = 0f, max = 0.5f
                                    )
                                )
                            }
                        }
                    }


                }

            }
        }
        BackMark {
            navigateToBack()
        }
    }


}

