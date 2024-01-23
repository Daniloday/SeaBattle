package com.missclick.seabattle.common

interface EventHandler<E> {
    fun obtainEvent(event: E)

}