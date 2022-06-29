package com.alexs.domain.use_cases.authorization.loan_details

import com.alexs.domain.model.Resource
import com.alexs.domain.model.ResponseValues
import com.alexs.domain.model.items.LoanConditions
import com.alexs.domain.repository.LoansRepository
import javax.inject.Inject

class GetLoanConditions @Inject constructor(
    private val loansRepository: LoansRepository
) {

    suspend operator fun invoke(authToken: String): Resource<LoanConditions> {
        return when (val response = loansRepository.getLoanConditions(authToken)) {
            is ResponseValues.Failure -> Resource.Error(error = response.error.errorMessage)
            is ResponseValues.Success -> Resource.Success(response.data)
        }
    }

}