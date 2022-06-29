package com.alexs.repository.repository

import android.content.Context
import com.alexs.domain.model.ResponseValues
import com.alexs.domain.repository.UserAccountRepository
import com.alexs.remote.api.LoanService
import com.alexs.remote.api.dto.post.AuthorizationBody
import com.alexs.repository.R
import com.alexs.repository.model.ErrorCodes
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

class UserAccountRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loanService: LoanService
) : UserAccountRepository {

    private val errorCodes = mapOf(
        400 to ErrorCodes.BadRequest(context.getString(R.string.bad_request)),
        401 to ErrorCodes.Unauthorized(context.getString(R.string.unauthorized)),
        403 to ErrorCodes.ForbiddenError(context.getString(R.string.forbidden)),
        404 to ErrorCodes.NotFoundError(context.getString(R.string.not_found)),
        503 to ErrorCodes.NoInternetConnection(context.getString(R.string.no_connection))
    )

    override suspend fun registerUser(
        userName: String,
        userPassword: String
    ): ResponseValues<String> = try {
        val response = loanService.register(
            AuthorizationBody(userName = userName, password = userPassword)
        )
        if (response.code() != 200) {
            val error = errorCodes[response.code()] ?: throw Exception()
            ResponseValues.Failure(error)
        } else ResponseValues.Success(data = response.body()!!.name)
    } catch (exception: Exception) {
        Timber.e(exception)
        ResponseValues.Failure(
            error = ErrorCodes.Unknown(errorMessage = context.getString(R.string.unknown))
        )
    }

    override suspend fun loginUser(
        userName: String,
        userPassword: String
    ): ResponseValues<String> = try {
        val response = loanService.login(
            AuthorizationBody(userName = userName, password = userPassword)
        )
        if (response.code() != 200) {
            val error = errorCodes[response.code()] ?: throw Exception()
            ResponseValues.Failure(error)
        } else ResponseValues.Success(data = response.body()!!)
    } catch (exception: Exception) {
        Timber.e(exception)
        ResponseValues.Failure(
            error = ErrorCodes.Unknown(errorMessage = context.getString(R.string.unknown))
        )
    }

}
