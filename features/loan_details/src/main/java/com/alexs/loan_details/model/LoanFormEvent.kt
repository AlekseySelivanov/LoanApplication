package com.alexs.loan_details.model

sealed class LoanFormEvent {
    data class FirstNameChanged(val firstName: String) : LoanFormEvent()
    data class LastNameChanged(val lastName: String) : LoanFormEvent()
    data class PhoneChanged(val phoneNumber: String) : LoanFormEvent()
    data class AmountChanged(val amount: String) : LoanFormEvent()
    object Apply : LoanFormEvent()
}