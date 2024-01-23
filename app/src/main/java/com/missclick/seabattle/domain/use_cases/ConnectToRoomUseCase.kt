package com.missclick.seabattle.domain.use_cases

import com.missclick.seabattle.common.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ConnectToRoomUseCase constructor () {
    operator fun invoke(roomCode  : String) : Flow<Resource<Nothing>> = flow{
        emit(Resource.Loading())
        delay(1000)
        emit(Resource.Success(null))
    }

}