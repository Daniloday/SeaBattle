package com.missclick.seabattle.presentation.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import com.missclick.seabattle.presentation.navigation.NavigationTree
import com.missclick.seabattle.presentation.ui.theme.AppTheme

@Composable
fun SettingsRoute(navController: NavController, vm: SettingsViewModel = hiltViewModel()) {

    val uiState by vm.uiState.collectAsState()

    SettingsScreen(uiState = uiState, obtainEvent = vm::obtainEvent, navigateTo = {
        println("navigate")
        navController.navigate(it.route)
    })

}

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    obtainEvent: (SettingsEvent) -> Unit,
    navigateTo: (NavigationTree) -> Unit
) {


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.sound),
                    style = AppTheme.typography.h1,
                    color = AppTheme.colors.primary
                )

                val remSoundOnOff by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.anim_sound_on_off))

                var soundOnNow by remember {
                    mutableStateOf(true)
                }

                var isPlaying by remember {
                    mutableStateOf(false)
                }

                val animSpecOnStart = LottieClipSpec.Progress(
                    min = 0.5f, max = 1f
                )

                val animSpecOffStart = LottieClipSpec.Progress(
                    min = 0f, max = 0.5f
                )

                if (soundOnNow) {
                    LottieAnimation(
                        modifier = Modifier
                            .size(100.dp)
                            .clickable {
                                soundOnNow = false
                                isPlaying = true
                            },
                        composition = remSoundOnOff,
                        clipSpec = animSpecOnStart,
                        isPlaying = isPlaying
                    )
                } else {
                    LottieAnimation(
                        modifier = Modifier
                            .size(100.dp)
                            .clickable {
                                soundOnNow = true
                                isPlaying = true
                            },
                        composition = remSoundOnOff,
                        clipSpec = animSpecOffStart,
                        isPlaying = true
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.vibro),
                    style = AppTheme.typography.h1,
                    color = AppTheme.colors.primary
                )

                val remVibroOnOff by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.anim_vibro_on_off))


                var isVibro by remember {
                    mutableStateOf(false)
                }

                Box(modifier = Modifier.size(60.dp).clickable(indication = null, interactionSource = MutableInteractionSource()) {
                    isVibro = !isVibro
                }, contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.phone_vibrate),
                        contentDescription = "phone vibration",
                        modifier = Modifier.size(30.dp)
                    )

                    if (isVibro){
                        LottieAnimation(
                            modifier = Modifier
                                ,
                            composition = remVibroOnOff,
                            clipSpec = LottieClipSpec.Progress(
                                min = 0f, max = 0.5f
                            )
                        )
                    }
                }
            }


        }
        BackMark {
            navigateTo(NavigationTree.Menu)
        }
    }
}

