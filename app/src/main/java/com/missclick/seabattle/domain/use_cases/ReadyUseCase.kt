package com.missclick.seabattle.domain.use_cases

import com.missclick.seabattle.data.remote.FireStore
import com.missclick.seabattle.data.remote.dto.cellsToDto
import com.missclick.seabattle.domain.model.Cell
import javax.inject.Inject

class ReadyUseCase @Inject constructor(
    val fireStore: FireStore
) {


    operator fun invoke(code: String, isOwner : Boolean, cells : List<List<Cell>>){
        println("zizi")
        fireStore.setReady(code, isOwner, cells.cellsToDto())
    }


}