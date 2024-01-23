package com.missclick.seabattle.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.missclick.seabattle.R

@Composable
fun BackMark(backCallBack : ()->Unit){
    Box(modifier = Modifier.padding(top = 24.dp,start = 16.dp)){
        Image(painter = painterResource(id = R.drawable.back_mark), modifier = Modifier.size(24.dp).clickable(indication = null, interactionSource = MutableInteractionSource()) {
            backCallBack()
        }, contentDescription = null)
    }
}