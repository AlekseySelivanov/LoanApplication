package com.alexs.domain.model.items

data class LoanConditions(
    val maxAmount: Int? = null,
    val percent: Double = 0.0,
    val period: Int = 0,
    val endDate: String = ""
)