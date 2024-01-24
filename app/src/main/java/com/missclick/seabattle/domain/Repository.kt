package com.missclick.seabattle.domain

import com.missclick.seabattle.domain.model.Field
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun createRoom(code : (String) -> Unit)

    suspend fun connectToRoom(code : String) : Flow<Field>

}