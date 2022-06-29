package com.alexs.domain.model.items

import com.alexs.domain.model.LoanState

data class LoanItem(
    val amount: Int = 0,
    val until: String = "",
    val firstName: String = "",
    val loanId: Int = 0,
    val lastName: String = "",
    val percent: Double = 0.0,
    val period: Int = 0,
    val phoneNumber: String = "",
    val state: LoanState = LoanState.REGISTERED
)