package com.alexs.domain.model

abstract class Error : Exception() {
    abstract val errorMessage: String
}