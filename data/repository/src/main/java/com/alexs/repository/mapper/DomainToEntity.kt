package com.alexs.repository.mapper

import com.alexs.domain.model.items.LoanItem
import com.alexs.local.db.entity.LoanEntity

fun LoanItem.toEntity() = LoanEntity(
    loanId = loanId,
    loanAmount = amount,
    loanEnd = until,
    state = state.ordinal
)

fun List<LoanItem>.toEntity() = map {
    it.toEntity()
}