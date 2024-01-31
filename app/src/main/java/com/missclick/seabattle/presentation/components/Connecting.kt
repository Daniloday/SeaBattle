package com.missclick.seabattle.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.missclick.seabattle.R
import com.missclick.seabattle.presentation.ui.theme.AppTheme

@Composable
fun Connecting(modifier: Modifier = Modifier) {

    Text(
        text = stringResource(id = R.string.waiting),
        color = AppTheme.colors.secondary,
        style = AppTheme.typography.h1,
        modifier = modifier
    )

}