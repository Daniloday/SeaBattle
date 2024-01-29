package com.missclick.seabattle.domain.use_cases

import com.missclick.seabattle.common.Resource
import com.missclick.seabattle.common.asResult
import com.missclick.seabattle.data.remote.FireStore
import com.missclick.seabattle.domain.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteRoomUseCase @Inject constructor (
    private val repository: Repository,
    val fireStore: FireStore
) {

    operator fun invoke(code : String){
        fireStore.deleteRoom(code)
    }

}