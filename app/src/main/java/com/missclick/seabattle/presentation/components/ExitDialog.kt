package com.missclick.seabattle.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.missclick.seabattle.R
import com.missclick.seabattle.presentation.ui.theme.AppTheme


@Composable
fun ExitDialog(no: () -> Unit, yes: () -> Unit){

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xA6222222))
            .click {
                no()
            }
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .fillMaxHeight(0.3f)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(10.dp))
                .click {  }
                .background(AppTheme.colors.secondaryBackground)
        ) {

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.areYouSureExit),
                    style = AppTheme.typography.h3,
                    color = AppTheme.colors.secondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(48.dp))

                Row {
                    Text(
                        text = stringResource(id = R.string.no),
                        style = AppTheme.typography.h2,
                        color = AppTheme.colors.primary,
                        modifier = Modifier.click {
                            no()
                        }
                    )


                    Spacer(modifier = Modifier.width(48.dp))

                    Text(text = stringResource(id = R.string.yes),
                        style = AppTheme.typography.h2,
                        color = AppTheme.colors.primary,
                        modifier = Modifier.click {
                            yes()
                        }
                    )
                }




            }


        }

    }

}