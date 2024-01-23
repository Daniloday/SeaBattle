package com.missclick.seabattle.common

sealed class Resource<T> {
    class Success<T>(data: T?) : Resource<T>()
    class Error<T>(message : String) : Resource<T>()
    class Loading<T> : Resource<T>()
}