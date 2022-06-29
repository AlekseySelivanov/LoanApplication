package com.alexs.loan_details.model

data class LoanFormState(
    val firstName: String = "",
    val firstNameError: String? = null,
    val lastName: String = "",
    val lastNameError: String? = null,
    val phoneNumber: String = "",
    val phoneNumberError: String? = null,
    val loanAmount: String = "",
    val loanAmountError: String? = null
)