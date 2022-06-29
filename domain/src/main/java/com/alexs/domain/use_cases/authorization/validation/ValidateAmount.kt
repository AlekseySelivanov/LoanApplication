package com.alexs.domain.use_cases.authorization.validation

import com.alexs.domain.model.ValidationResult
import com.alexs.domain.repository.StringsRepository
import javax.inject.Inject

class ValidateAmount @Inject constructor(
    private val stringsRepository: StringsRepository
) {

    operator fun invoke(inputAmount: String, maxAmount: String): ValidationResult {
        if (inputAmount.isBlank()) return ValidationResult(
            successful = false,
            errorMessage = stringsRepository.blankAmountMessage
        )
        if (inputAmount.contains(char = '-')) return ValidationResult(
            successful = false,
            errorMessage = stringsRepository.zeroAmountMessage
        )
        if (inputAmount.any { !it.isDigit() }) return ValidationResult(
            successful = false,
            errorMessage = stringsRepository.inNumericAmountMessage
        )
        try {
            if (inputAmount.toInt() > maxAmount.toInt()) return ValidationResult(
                successful = false,
                errorMessage = stringsRepository.highAmountMessage
            )
        } catch (exception: NumberFormatException) {
            return ValidationResult(
                successful = false,
                errorMessage = stringsRepository.highAmountMessage
            )
        }
        return ValidationResult(successful = true)
    }

}