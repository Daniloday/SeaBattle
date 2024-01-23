package com.missclick.seabattle.domain.use_cases

import com.missclick.seabattle.common.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CreateRoomUseCase constructor () {
    operator fun invoke() : Flow<Resource<String>> = flow{
        emit(Resource.Loading())
        delay(1000)
        emit(Resource.Success("57392"))
    }
}