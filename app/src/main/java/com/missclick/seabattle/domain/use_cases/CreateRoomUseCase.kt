package com.missclick.seabattle.domain.use_cases



import com.missclick.seabattle.common.Resource
import com.missclick.seabattle.common.asResult
import com.missclick.seabattle.domain.Repository
import dagger.Provides
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.lang.RuntimeException

import javax.inject.Inject


class CreateRoomUseCase @Inject constructor (
    private val repository: Repository
) {
    operator fun invoke() = flow<String>{
        emit("testcode")
    }.asResult()
}