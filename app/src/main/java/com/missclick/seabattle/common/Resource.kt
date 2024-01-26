package com.missclick.seabattle.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed class Resource<out T>() {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error<T>(val exception: String) : Resource<T>()
    class Loading<T>() : Resource<T>()
}

fun <T> Flow<T>.asResult(): Flow<Resource<T>> = map<T, Resource<T>> { Resource.Success(it) }
    .onStart { emit(Resource.Loading<T>()) }
    .catch { emit(Resource.Error(it.message.toString())) }

