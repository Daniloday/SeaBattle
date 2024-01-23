package com.missclick.seabattle.presentation.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class Colors (




    val primaryBackground : Color,
    val secondaryBackground : Color,

    val primary: Color,
    val secondary: Color,


)


val lightColorPalette = Colors(
    primaryBackground = Color(0xFFB6ABC6),
    secondaryBackground = Color(0xFFD9D9D9),

    primary = Color(0xFF000000),
    secondary = Color(0xFF817D7D),




)


val LocalColorProvider = staticCompositionLocalOf<Colors> {
    error("No colors")
}