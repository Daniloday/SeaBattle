package com.missclick.seabattle.domain.use_cases

import com.missclick.seabattle.common.Resource
import com.missclick.seabattle.domain.model.Cell
import com.missclick.seabattle.domain.model.Field
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ObserveFieldUseCase constructor () {
    operator fun invoke(roomCode: String): Flow<Resource<Field>> = flow {
        emit(Resource.Loading())
        delay(1000)
        val l = mutableListOf<List<Cell>>()
        repeat(10){
            l.add(listOf(Cell.EMPTY, Cell.DOT, Cell.EMPTY, Cell.EMPTY, Cell.EMPTY, Cell.EMPTY, Cell.EMPTY, Cell.EMPTY,Cell.EMPTY,Cell.EMPTY,))
        }
        emit(Resource.Success(Field(l)))
    }
}
