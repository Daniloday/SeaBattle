package com.missclick.seabattle.presentation.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class Colors (
    val primaryBackground : Color,
    val secondaryBackground : Color,

    val primary: Color,
    val secondary: Color,
    val secondaryShadow: Color,
    val error : Color,
    val success : Color,
    val shipSelected : Color,
)


val lightColorPalette = Colors(
    primaryBackground = Color(0xFFB6ABC6),
    secondaryBackground = Color(0xFFD9D9D9),

    primary = Color(0xFF000000),
    secondary = Color(0xFF817D7D),
    secondaryShadow = Color(0x4D817D7D),
    error = Color(0xFFE01313),
    success = Color(0xFF50E013),
    shipSelected = Color(0xFF0E7713)
)


val LocalColorProvider = staticCompositionLocalOf<Colors> {
    error("No colors")
}