package com.missclick.seabattle.domain.use_cases

import com.google.android.gms.tasks.Tasks.await
import com.missclick.seabattle.common.Resource
import com.missclick.seabattle.common.asResult
import com.missclick.seabattle.data.remote.FireStore
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class ConnectToRoomUseCase @Inject constructor(
    val fireStore: FireStore
) {

    operator fun invoke(code: String, isOwner : Boolean): Flow<Resource<Nothing?>> = fireStore.connect(code, isOwner).asResult()

}