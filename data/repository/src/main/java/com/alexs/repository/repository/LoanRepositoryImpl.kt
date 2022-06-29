package com.alexs.repository.repository

import android.content.Context
import com.alexs.domain.model.Error
import com.alexs.domain.model.ResponseValues
import com.alexs.domain.model.items.LoanConditions
import com.alexs.domain.model.items.LoanItem
import com.alexs.domain.repository.LoansRepository
import com.alexs.local.db.dao.LoanDao
import com.alexs.remote.api.LoanService
import com.alexs.remote.api.dto.post.LoanBody
import com.alexs.repository.R
import com.alexs.repository.mapper.toDomain
import com.alexs.repository.mapper.toEntity
import com.alexs.repository.model.ErrorCodes
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject

class LoanRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val loanService: LoanService,
    private val loanDao: LoanDao
) : LoansRepository {

    private val errorCodes = mapOf(
        401 to ErrorCodes.Unauthorized(context.getString(R.string.unauthorized)),
        403 to ErrorCodes.ForbiddenError(context.getString(R.string.forbidden)),
        404 to ErrorCodes.NotFoundError(context.getString(R.string.not_found)),
        503 to ErrorCodes.NoInternetConnection(context.getString(R.string.no_connection))
    )

    override suspend fun getLoansFromRemote(authToken: String): ResponseValues<List<LoanItem>> =
        try {
            val loansResponse = loanService.getLoansList(token = authToken)
            if (loansResponse.code() != 200) {
                val error = errorCodes[loansResponse.code()] ?: throw Exception()
                ResponseValues.Failure(error = error)
            } else ResponseValues.Success(data = loansResponse.body()!!.toDomain())
        } catch (exception: SocketTimeoutException) {
            logAndReturnException(
                exception,
                ErrorCodes.ServerNotResponding(errorMessage = context.getString(R.string.unavailable))
            )
        } catch (exception: Exception) {
            logAndReturnException(
                exception,
                ErrorCodes.Unknown(errorMessage = context.getString(R.string.unknown))
            )
        }

    override suspend fun getLoansFromLocal(): List<LoanItem> =
        loanDao.getAllLoans().first().toDomain()

    override suspend fun getLoanConditions(authToken: String): ResponseValues<LoanConditions> =
        try {
            val conditionsResponse = loanService.getLoanConditions(authToken)
            if (conditionsResponse.code() != 200) {
                val error = errorCodes[conditionsResponse.code()] ?: throw Exception()
                ResponseValues.Failure(error)
            } else ResponseValues.Success(data = conditionsResponse.body()!!.toDomain())
        } catch (exception: SocketTimeoutException) {
            logAndReturnException(
                exception,
                ErrorCodes.ServerNotResponding(errorMessage = context.getString(R.string.unavailable))
            )
        } catch (exception: Exception) {
            logAndReturnException(
                exception,
                ErrorCodes.Unknown(errorMessage = context.getString(R.string.unknown))
            )
        }

    override suspend fun getLoanDetails(loanId: Int, authToken: String): ResponseValues<LoanItem> =
        try {
            val loanResult = loanService.getLoan(token = authToken, loanId = loanId)
            if (loanResult.code() != 200) {
                val error = errorCodes[loanResult.code()] ?: throw Exception()
                ResponseValues.Failure(error)
            } else ResponseValues.Success(data = loanResult.body()!!.toDomain())
        } catch (exception: SocketTimeoutException) {
            logAndReturnException(
                exception,
                ErrorCodes.ServerNotResponding(context.getString(R.string.unavailable))
            )
        } catch (exception: Exception) {
            logAndReturnException(
                exception,
                ErrorCodes.Unknown(context.getString(R.string.unknown))
            )
        }

    override suspend fun saveLoan(loanItem: LoanItem) = loanDao.insertLoan(loanItem.toEntity())

    override suspend fun fetchLoans(loansList: List<LoanItem>) =
        loanDao.insertLoansList(loansList.toEntity())

    override fun readLoansList(): Flow<List<LoanItem>> = loanDao.getAllLoans().toDomain()

    override suspend fun createLoan(
        authToken: String,
        amount: Int,
        firstName: String,
        lastName: String,
        percent: Double,
        period: Int,
        phoneNumber: String
    ): ResponseValues<LoanItem> = try {
        val loanResult = loanService.createLoan(
            token = authToken, loanBody = LoanBody(
                amount = amount,
                firstName = firstName,
                lastName = lastName,
                percent = percent,
                period = period,
                phoneNumber = phoneNumber
            )
        )
        if (loanResult.code() != 200) {
            val error = errorCodes[loanResult.code()] ?: throw Exception()
            ResponseValues.Failure(error)
        } else ResponseValues.Success(data = loanResult.body()!!.toDomain())
    } catch (exception: SocketTimeoutException) {
        logAndReturnException(
            exception,
            ErrorCodes.ServerNotResponding(context.getString(R.string.unavailable))
        )
    } catch (exception: Exception) {
        logAndReturnException(exception, ErrorCodes.Unknown(context.getString(R.string.unknown)))
    }

    private fun <T> logAndReturnException(
        exception: Exception,
        error: Error
    ): ResponseValues<T> {
        Timber.e(exception)
        return ResponseValues.Failure(error)
    }
}