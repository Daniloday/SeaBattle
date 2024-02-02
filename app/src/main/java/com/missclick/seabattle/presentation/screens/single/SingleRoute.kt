package com.missclick.seabattle.presentation.screens.single

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.missclick.seabattle.R
import com.missclick.seabattle.presentation.components.BackMark
import com.missclick.seabattle.presentation.ui.theme.AppTheme

@Composable
fun SingleRoute(navController: NavController) {

    SingleScreen {
        navController.popBackStack()
    }

}

@Composable
fun SingleScreen(navigateToBack : () -> Unit) {
    Box(Modifier.fillMaxSize()) {

        BackMark {
            navigateToBack()
        }

        Text(
            text = stringResource(id = R.string.coming),
            modifier = Modifier.align(Alignment.Center),
            color = AppTheme.colors.secondary,
            style = AppTheme.typography.h2
        )
    }
}