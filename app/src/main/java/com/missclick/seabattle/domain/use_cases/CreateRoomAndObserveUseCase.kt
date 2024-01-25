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


class CreateRoomAndObserveUseCase @Inject constructor (
    private val repository: Repository,
    val fireStore: FireStore
) {

    operator fun invoke() : Flow<Resource<Game>> = callbackFlow{
        fireStore.newCreateRoom{ creationStatus ->
            when(creationStatus){
                is Resource.Success -> {
                    fireStore.newObserve(creationStatus.data){ observeStatus ->
                        when (observeStatus) {
                            is Resource.Success -> {
                                trySend(Resource.Success(observeStatus.data.toGame(true, creationStatus.data)))
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
                is Resource.Loading -> { trySend(Resource.Loading()) }
                is Resource.Error -> { trySend(Resource.Error(creationStatus.exception)) }
            }
        }
        awaitClose()
    }
}