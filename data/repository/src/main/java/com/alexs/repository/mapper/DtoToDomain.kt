package com.alexs.repository.mapper

import com.alexs.domain.model.LoanState
import com.alexs.domain.model.items.LoanConditions
import com.alexs.domain.model.items.LoanItem
import com.alexs.remote.api.dto.get.ConditionsResponse
import com.alexs.remote.api.dto.get.LoanResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun List<LoanResponse>.toDomain(): List<LoanItem> = map { response ->
    LoanItem(
        loanId = response.id,
        amount = response.amount,
        until = getEndDate(response.period),
        firstName = response.firstName,
        lastName = response.lastName,
        percent = response.percent,
        period = response.period,
        phoneNumber = response.phoneNumber,
        state = LoanState.valueOf(response.state)
    )
}

fun ConditionsResponse.toDomain(): LoanConditions = LoanConditions(
    maxAmount = maxAmount,
    percent = percent,
    period = period,
    endDate = getEndDate(period)
)

fun LoanResponse.toDomain() = LoanItem(
    loanId = id,
    amount = amount,
    until = getEndDate(period),
    firstName = firstName,
    lastName = lastName,
    percent = percent,
    period = period,
    phoneNumber = phoneNumber,
    state = LoanState.valueOf(state)
)

private fun getEndDate(period: Int): String {
    val endDateTime = LocalDate.now().plusDays(period.toLong()).toString()
    val localDate = LocalDate.parse(endDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    return localDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
}