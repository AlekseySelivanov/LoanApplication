package com.alexs.domain.use_cases.authorization.common

import com.alexs.domain.use_cases.authorization.user_data.ReadAuthToken
import com.alexs.domain.use_cases.authorization.loans.FetchLoansList
import com.alexs.domain.use_cases.authorization.loans.ReadLoansList
import javax.inject.Inject

data class HomeCases @Inject constructor(
    val fetchLoansList: FetchLoansList,
    val readLoansList: ReadLoansList,
    val readAuthToken: ReadAuthToken
)
