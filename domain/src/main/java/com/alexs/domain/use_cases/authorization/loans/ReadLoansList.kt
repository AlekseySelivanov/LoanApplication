package com.alexs.domain.use_cases.authorization.loans

import com.alexs.domain.model.Resource
import com.alexs.domain.repository.LoansRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReadLoansList @Inject constructor(private val loansRepository: LoansRepository) {

    operator fun invoke() = loansRepository.readLoansList().map {
        Resource.Success(data = it)
    }

}