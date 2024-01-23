package com.missclick.seabattle.presentation.ui.theme


import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


data class Typography(
    val headerTextBold: TextStyle,
)


val typography = Typography(
    headerTextBold = TextStyle(
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold
    ),

)

val LocalTypographyProvider = staticCompositionLocalOf<Typography> {
    error("No typography")
}