package com.alexs.domain.use_cases.authorization.loan_details

import com.alexs.domain.model.Resource
import com.alexs.domain.model.ResponseValues
import com.alexs.domain.repository.LoansRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ApplyLoan @Inject constructor(private val loansRepository: LoansRepository) {

    operator fun invoke(
        authToken: String,
        amount: Int,
        firstName: String,
        lastName: String,
        percent: Double,
        period: Int,
        phoneNumber: String
    ) = flow {
        emit(Resource.Loading)

        when (val response = loansRepository.createLoan(
            authToken,
            amount,
            firstName,
            lastName,
            percent,
            period,
            phoneNumber
        )) {
            is ResponseValues.Failure -> emit(Resource.Error(error = response.error.errorMessage))
            is ResponseValues.Success -> {
                emit(Resource.Success(data = true))
                loansRepository.saveLoan(response.data)
            }
        }
    }

}