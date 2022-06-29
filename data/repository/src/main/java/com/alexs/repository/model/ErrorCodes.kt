package com.alexs.repository.model

import com.alexs.domain.model.Error

object ErrorCodes {
    class BadRequest(override val errorMessage: String) : Error()
    class ForbiddenError(override val errorMessage: String) : Error()
    class NotFoundError(override val errorMessage: String) : Error()
    class Unauthorized(override val errorMessage: String) : Error()
    class NoInternetConnection(override val errorMessage: String) : Error()
    class ServerNotResponding(override val errorMessage: String): Error()
    class Unknown(override val errorMessage: String) : Error()
}