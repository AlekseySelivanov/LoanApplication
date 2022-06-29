package com.alexs.repository.mapper

import com.alexs.domain.model.LoanState
import com.alexs.domain.model.items.LoanItem
import com.alexs.local.db.entity.LoanEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun LoanEntity.toDomain() = LoanItem(
    loanId = loanId,
    amount = loanAmount,
    until = loanEnd,
    state = LoanState.values()[state]
)

fun List<LoanEntity>.toDomain() = map {
    it.toDomain()
}

fun Flow<List<LoanEntity>>.toDomain() = map {
    it.map { entity ->
        entity.toDomain()
    }
}