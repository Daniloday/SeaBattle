package com.missclick.seabattle.domain.use_cases

import com.missclick.seabattle.data.remote.FireStore
import com.missclick.seabattle.data.remote.dto.CellDto
import com.missclick.seabattle.data.remote.dto.cellsToDto
import com.missclick.seabattle.domain.model.Cell
import javax.inject.Inject

class DoStepUseCase @Inject constructor (
    val fireStore: FireStore
) {

    operator fun invoke(yIndex: Int, xIndex: Int,  friendCells: List<List<Cell>>, code: String, isOwner : Boolean){

        val newDtoField = friendCells.cellsToDto().mapIndexed { index, element, ->
            if (index == yIndex * 10 + xIndex){
                CellDto(dot = true, element.ship)
            }else{
                element
            }
        }

        fireStore.doStep(
            code = code,
            isOwner = isOwner,
            cells = newDtoField
        )
    }

}