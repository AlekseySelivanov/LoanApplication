package com.alexs.domain.use_cases.authorization.validation

import com.alexs.domain.model.ValidationResult
import com.alexs.domain.repository.StringsRepository
import javax.inject.Inject

class ValidateFullName @Inject constructor(
    private val stringsRepository: StringsRepository
) {

    operator fun invoke(userName: String): ValidationResult {
        if (userName.isBlank()) return ValidationResult(
            successful = false,
            errorMessage = stringsRepository.blankFullNameMessage
        )
        if (userName.split(" ").count() < 2) return ValidationResult(
            successful = false,
            errorMessage = stringsRepository.shortFullNameMessage
        )
        return ValidationResult(successful = true)
    }

}