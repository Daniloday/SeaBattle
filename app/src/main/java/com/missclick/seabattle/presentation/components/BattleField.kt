package com.missclick.seabattle.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.missclick.seabattle.R
import com.missclick.seabattle.domain.model.Cell
import com.missclick.seabattle.presentation.ui.theme.AppTheme




var listBattlefield = listOf<List<Cell>>(
    listOf(
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY
    ),
    listOf(
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY
    ),
    listOf(
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY
    ),
    listOf(
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY
    ),
    listOf(
        Cell.EMPTY,
        Cell.DOT,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.SHIP_ALIVE,
        Cell.EMPTY,
        Cell.SHIP_DAMAGE,
        Cell.EMPTY,
        Cell.EMPTY
    ),
    listOf(
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY
    ),
    listOf(
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY
    ),
    listOf(
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY
    ),
    listOf(
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY
    ),
    listOf(
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY,
        Cell.EMPTY
    )
)

var numbList = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
var abcList = listOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j")

@Composable
fun Battlefield(listBattlefield: List<List<Cell>>, modifier: Modifier) {

    Column(
        modifier = modifier
    ) {
        //numbers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            for (letter in numbList) {
                TextCellBattlefield(modifier = Modifier.weight(1f), text = letter)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(10f)
        ) {

            //letters
            Column(Modifier.weight(1f)) {
                for (rowNumber in listBattlefield.indices) {
                    TextCellBattlefield(
                        modifier = Modifier.weight(1f),
                        text = abcList[rowNumber]
                    )

                }
            }

            //battlefield
            Column(
                Modifier
                    .weight(10f)
                    .border(1.dp, AppTheme.colors.primary)
            ) {
                for (rowNumber in listBattlefield.indices) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        for (j in listBattlefield[rowNumber]) {

                            when (j) {
                                Cell.EMPTY -> {
                                    EmptyCellBattlefield(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { })
                                }

                                Cell.DOT -> {
                                    DotCellBattlefield(modifier = Modifier.weight(1f))
                                }

                                Cell.SHIP_ALIVE -> {
                                    ShipAliveCellBattlefield(modifier = Modifier.weight(1f))
                                }

                                Cell.SHIP_DAMAGE -> {
                                    ShipDamageCellBattlefield(modifier = Modifier.weight(1f))
                                }

                            }

                        }
                    }
                }
            }

        }

    }

}

@Composable
fun TextCellBattlefield(modifier: Modifier, text: String) {
    Card(
        modifier = modifier,
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = text,
                color = AppTheme.colors.secondary,
                modifier = Modifier.align(
                    Alignment.Center
                ),
                style = AppTheme.typography.h3,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun EmptyCellBattlefield(modifier: Modifier) {
    Card(
        modifier = modifier,
        border = BorderStroke(0.5.dp, AppTheme.colors.secondaryShadow),
        shape = RectangleShape
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

        }
    }
}

@Composable
fun DotCellBattlefield(modifier: Modifier) {
    Card(
        modifier = modifier,
        border = BorderStroke(0.5.dp, AppTheme.colors.secondaryShadow),
        shape = RectangleShape
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.dot),
                contentDescription = "dot",
                modifier = Modifier
                    .fillMaxSize(0.7f)
                    .align(
                        Alignment.Center
                    )
            )
        }
    }
}


@Composable
fun ShipAliveCellBattlefield(modifier: Modifier) {
    Card(
        modifier = modifier,
        border = BorderStroke(2.dp, AppTheme.colors.primary),
        shape = RectangleShape
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

        }
    }
}


@Composable
fun ShipDamageCellBattlefield(modifier: Modifier) {
    Card(
        modifier = modifier,
        border = BorderStroke(2.dp, AppTheme.colors.primary),
        shape = RectangleShape
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.cross),
                contentDescription = "cross",
                modifier = Modifier
                    .fillMaxSize(0.7f)
                    .align(
                        Alignment.Center
                    ),

                )
        }
    }
}



