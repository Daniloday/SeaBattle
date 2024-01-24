package com.missclick.seabattle.domain.model

data class Field(
    val cells : List<List<Cell>>,
    val code : String,
    val isConnected1 : Boolean,
    val isConnected2: Boolean,
    val isReady1 : Boolean,
    val isReady2: Boolean,
    val moveFirst : Boolean = true
)
