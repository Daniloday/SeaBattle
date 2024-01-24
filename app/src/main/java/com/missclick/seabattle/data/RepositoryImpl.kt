package com.missclick.seabattle.data

import com.missclick.seabattle.data.remote.FireStore
import com.missclick.seabattle.domain.Repository
import com.missclick.seabattle.domain.model.Field
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val fireStore: FireStore) : Repository{


    override suspend fun createRoom(code: (String) -> Unit) {
        fireStore.createRoom(code)
    }

    override suspend fun connectToRoom(code: String): Flow<Field> {
        TODO("Not yet implemented")
    }


}