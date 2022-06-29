package com.alexs.domain.use_cases.authorization.validation

import com.alexs.domain.model.ValidationResult
import com.alexs.domain.repository.StringsRepository
import java.util.regex.Pattern
import javax.inject.Inject

class ValidateEmail @Inject constructor(
    private val stringsRepository: StringsRepository
) {

    companion object {
        private const val EMAIL_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}\$"
    }

    operator fun invoke(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = stringsRepository.blankEmailMessage
            )
        }
        if (!Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE)
                .matcher(email)
                .matches()
        ) {
            return ValidationResult(
                successful = false,
                errorMessage = stringsRepository.invalidEmailMessage
            )
        }
        return ValidationResult(successful = true)
    }

}