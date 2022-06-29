package com.alexs.domain.model

sealed class ResponseValues<out T> {
    data class Success<T>(val data: T) : ResponseValues<T>()
    data class Failure<T>(val error: Error) : ResponseValues<T>()
}