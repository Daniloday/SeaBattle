package com.missclick.seabattle.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.missclick.seabattle.presentation.navigation.Navigation
import com.missclick.seabattle.presentation.ui.theme.AppTheme
import com.missclick.seabattle.presentation.ui.theme.SeaBattleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SeaBattleTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = AppTheme.colors.primary
                ) {
                    Navigation()
                }
            }
        }
    }
    
}

