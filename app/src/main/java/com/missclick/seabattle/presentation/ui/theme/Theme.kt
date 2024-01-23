package com.missclick.seabattle.presentation.ui.theme


import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable




@Composable
fun SeaBattleTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalColorProvider provides lightColorPalette,
        LocalTypographyProvider provides typography,
        content = content,
    )

}

object AppTheme{
    val colors : Colors
        @Composable
        @ReadOnlyComposable
        get() = LocalColorProvider.current

    val typography : Typography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypographyProvider.current
}