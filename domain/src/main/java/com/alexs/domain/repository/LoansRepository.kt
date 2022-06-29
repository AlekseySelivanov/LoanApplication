package com.alexs.domain.repository

import com.alexs.domain.model.ResponseValues
import com.alexs.domain.model.items.LoanConditions
import com.alexs.domain.model.items.LoanItem
import kotlinx.coroutines.flow.Flow

interface LoansRepository {

    suspend fun getLoanConditions(authToken: String): ResponseValues<LoanConditions>
    suspend fun getLoanDetails(loanId: Int, authToken: String): ResponseValues<LoanItem>
    suspend fun saveLoan(loanItem: LoanItem)

    suspend fun getLoansFromRemote(authToken: String): ResponseValues<List<LoanItem>>
    suspend fun getLoansFromLocal(): List<LoanItem>
    suspend fun fetchLoans(loansList: List<LoanItem>)
    fun readLoansList(): Flow<List<LoanItem>>

    suspend fun createLoan(
        authToken: String,
        amount: Int,
        firstName: String,
        lastName: String,
        percent: Double,
        period: Int,
        phoneNumber: String
    ): ResponseValues<LoanItem>

}