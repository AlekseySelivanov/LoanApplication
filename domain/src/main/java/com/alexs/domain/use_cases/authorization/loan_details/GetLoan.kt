package com.alexs.domain.use_cases.authorization.loan_details

import com.alexs.domain.model.Resource
import com.alexs.domain.model.ResponseValues
import com.alexs.domain.repository.LoansRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLoan @Inject constructor(
    private val loansRepository: LoansRepository
) {

    operator fun invoke(authToken: String, loanId: Int) = flow {
        emit(Resource.Loading)
        when (val loanDetailsResponse = loansRepository.getLoanDetails(loanId, authToken)) {
            is ResponseValues.Failure -> emit(Resource.Error(loanDetailsResponse.error.errorMessage))
            is ResponseValues.Success -> emit(Resource.Success(data = loanDetailsResponse.data))
        }
    }

}