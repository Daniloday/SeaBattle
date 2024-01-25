package com.missclick.seabattle.domain.use_cases

import com.missclick.seabattle.common.Resource
import com.missclick.seabattle.common.asResult
import com.missclick.seabattle.data.remote.FireStore
import com.missclick.seabattle.data.remote.dto.toGame
import com.missclick.seabattle.domain.Repository
import com.missclick.seabattle.domain.model.Cell
import com.missclick.seabattle.domain.model.Game
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class ObserveRoomUseCase @Inject constructor (
    private val repository: Repository,
    val fireStore: FireStore
) {

    operator fun invoke(code : String, isOwner : Boolean):
            Flow<Resource<Game>> = fireStore.observe(code).map {gameDto ->
                val game = gameDto.toGame(isOwner = isOwner)
                game.copy(
                    friendCells = game.friendCells.map { listCell ->
                        listCell.map { cell ->
                            if (cell == Cell.SHIP_ALIVE){
                                Cell.EMPTY
                            }else{
                                cell
                            }
                        } }
                )
            }.asResult()
}
