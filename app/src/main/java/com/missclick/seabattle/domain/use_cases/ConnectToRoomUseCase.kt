package com.missclick.seabattle.domain.use_cases

import com.missclick.seabattle.common.Resource
import com.missclick.seabattle.common.asResult
import com.missclick.seabattle.data.remote.FireStore
import com.missclick.seabattle.domain.model.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ConnectToRoomUseCase @Inject constructor(
    val fireStore: FireStore
) {

    operator fun invoke(code: String): Flow<Resource<Nothing?>> = fireStore.connect(code).asResult()

}