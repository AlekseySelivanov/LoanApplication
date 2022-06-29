package com.alexs.domain.use_cases.authorization.common

import com.alexs.domain.use_cases.authorization.user_data.ReadAuthToken
import com.alexs.domain.use_cases.authorization.loan_details.ApplyLoan
import com.alexs.domain.use_cases.authorization.loan_details.GetLoan
import com.alexs.domain.use_cases.authorization.loan_details.GetLoanConditions
import com.alexs.domain.use_cases.authorization.user_data.ReadUserData
import com.alexs.domain.use_cases.authorization.validation.ValidateAmount
import com.alexs.domain.use_cases.authorization.validation.ValidateName
import com.alexs.domain.use_cases.authorization.validation.ValidatePhoneNumber
import javax.inject.Inject

data class LoanDetailsCases @Inject constructor(
    val readAuthToken: ReadAuthToken,
    val readUserData: ReadUserData,
    val validateFirstName: ValidateName,
    val validateLastName: ValidateName,
    val validatePhoneNumber: ValidatePhoneNumber,
    val validateAmount: ValidateAmount,
    val getLoanConditions: GetLoanConditions,
    val getLoan: GetLoan,
    val applyLoan: ApplyLoan
)
