package com.alexs.domain.use_cases.authorization.loans

import com.alexs.domain.model.ResponseValues
import com.alexs.domain.repository.LoansRepository
import javax.inject.Inject

class FetchLoansList @Inject constructor(private val loansRepository: LoansRepository) {

    suspend operator fun invoke(authToken: String) {
        val loansFromRemote = loansRepository.getLoansFromRemote(authToken)
        if (loansFromRemote is ResponseValues.Success) {
            val loansFromLocal = loansRepository.getLoansFromLocal()
            if (loansFromRemote.data.isNotEmpty() && loansFromLocal.isEmpty()) {
                loansRepository.fetchLoans(loansFromRemote.data)
                return
            }
            val difference = loansFromRemote.data.filterNot { loanItem ->
                loansFromLocal.any {
                    it.loanId == loanItem.loanId
                            && it.state == loanItem.state
                            && it.until == loanItem.until
                }
            }
            if (difference.isNotEmpty()) loansRepository.fetchLoans(difference)
        }
    }

}