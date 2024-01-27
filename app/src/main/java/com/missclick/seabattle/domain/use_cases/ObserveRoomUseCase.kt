package com.missclick.seabattle.domain.use_cases

import com.missclick.seabattle.common.Resource
import com.missclick.seabattle.common.asResult
import com.missclick.seabattle.data.remote.FireStore
import com.missclick.seabattle.data.remote.dto.cellsToDto
import com.missclick.seabattle.data.remote.dto.toGame
import com.missclick.seabattle.domain.Repository
import com.missclick.seabattle.domain.model.Cell
import com.missclick.seabattle.domain.model.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


class ObserveRoomUseCase @Inject constructor(
    private val repository: Repository,
    val fireStore: FireStore
) {

    operator fun invoke(code: String, isOwner: Boolean):
            Flow<Resource<Game>> = fireStore.observe(code).map { gameDto ->
        gameDto.toGame(isOwner = isOwner)
    }.asResult()





}
