package com.missclick.seabattle.domain.use_cases



import com.missclick.seabattle.common.Resource
import com.missclick.seabattle.common.asResult
import com.missclick.seabattle.data.remote.FireStore
import com.missclick.seabattle.data.remote.dto.toGame
import com.missclick.seabattle.domain.Repository
import com.missclick.seabattle.domain.model.Game
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


import javax.inject.Inject


class CreateRoomUseCase @Inject constructor (
    private val repository: Repository,
    val fireStore: FireStore
) {

    operator fun invoke() : Flow<Resource<String>> = fireStore.createRoom().asResult()

}



