package com.alexs.domain.use_cases.authorization.validation

import com.alexs.domain.model.ValidationResult
import com.alexs.domain.repository.StringsRepository
import javax.inject.Inject

class ValidateRepeatedPassword @Inject constructor(
    private val stringsRepository: StringsRepository
) {

    operator fun invoke(password: String, repeatedPassword: String): ValidationResult {
        if (password != repeatedPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = stringsRepository.notMatchingPassword
            )
        }
        return ValidationResult(successful = true)
    }

}