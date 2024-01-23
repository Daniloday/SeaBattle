package com.missclick.seabattle.presentation.ui.theme


import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.missclick.seabattle.R


data class Typography(
    val h1: TextStyle,
    val h2: TextStyle,
    val h3: TextStyle,
    val h4: TextStyle,
)


val typography = Typography(
    h1 = TextStyle(
        fontFamily = FontFamily(Font(R.font.rubik_bubbles)),
        fontSize = 24.sp,
        fontWeight = FontWeight.Normal
    ),
    h2 = TextStyle(
        fontFamily = FontFamily(Font(R.font.rubik_bubbles)),
        fontSize = 36.sp,
        fontWeight = FontWeight.Normal
    ),
    h3 = TextStyle(
        fontFamily = FontFamily(Font(R.font.rubik_bubbles)),
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal
    ),
    h4 = TextStyle(
        fontFamily = FontFamily(Font(R.font.rubik_bubbles)),
        fontSize = 46.sp,
        fontWeight = FontWeight.Normal
    )

)

val LocalTypographyProvider = staticCompositionLocalOf<Typography> {
    error("No typography")
}