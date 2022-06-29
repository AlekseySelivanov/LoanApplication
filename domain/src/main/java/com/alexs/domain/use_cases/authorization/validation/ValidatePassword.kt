package com.alexs.domain.use_cases.authorization.validation

import com.alexs.domain.model.ValidationResult
import com.alexs.domain.repository.StringsRepository
import javax.inject.Inject

class ValidatePassword @Inject constructor(
    private val stringsRepository: StringsRepository
) {

    operator fun invoke(password: String): ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = stringsRepository.blankPasswordMessage
            )
        }
        if (password.length < 8) {
            return ValidationResult(
                successful = false,
                errorMessage = stringsRepository.shortPasswordMessage
            )
        }
        val containsLettersAndDigits =
            password.any { it.isDigit() } && password.any { it.isLetter() }
        if (!containsLettersAndDigits) {
            return ValidationResult(
                successful = false,
                errorMessage = stringsRepository.incorrectPasswordFormat
            )
        }
        return ValidationResult(successful = true)
    }

}