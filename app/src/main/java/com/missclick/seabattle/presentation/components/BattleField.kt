package com.missclick.seabattle.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


enum class CellState(){
    CLEAR, EMPTY, DOT, SHIP_ALIVE, SHIP_DAMAGE
}

var list = listOf<List<CellState>>(
    listOf(CellState.EMPTY, CellState.EMPTY, CellState.EMPTY)
)

@Composable
fun Battlefield(){


    LazyColumn(content = {
        item {

        }
        itemsIndexed(list){

            _, _ ->

            LazyRow{
                item {

                }


            }



        }
    })


}


