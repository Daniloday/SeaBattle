package com.missclick.seabattle.domain.use_cases

import com.missclick.seabattle.common.Resource
import com.missclick.seabattle.data.remote.FireStore
import com.missclick.seabattle.data.remote.dto.toGame
import com.missclick.seabattle.domain.Repository
import com.missclick.seabattle.domain.model.Game
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


class ConnectAndObserveRoomUseCase @Inject constructor (
    private val repository: Repository,
    val fireStore: FireStore
) {

    operator fun invoke(code : String, isOwner : Boolean): Flow<Resource<Game>> = callbackFlow {
        fireStore.newConnect(code) { creationStatus ->
            when (creationStatus) {
                is Resource.Success -> {
                    fireStore.newObserve(code) { observeStatus ->

                        when (observeStatus) {
                            is Resource.Success -> {
                                trySend(Resource.Success(observeStatus.data.toGame(isOwner, code)))
                            }

                            is Resource.Loading -> {
                                trySend(Resource.Loading())
                            }

                            is Resource.Error -> {
                                trySend(Resource.Error(observeStatus.exception))
                            }
                        }
                    }
                }

                is Resource.Loading -> {
                    trySend(Resource.Loading())
                }

                is Resource.Error -> {
                    trySend(Resource.Error(creationStatus.exception))
                }
            }
        }
        awaitClose()
    }
}
