package com.alexs.domain.use_cases.authorization.validation

import com.alexs.domain.model.ValidationResult
import com.alexs.domain.repository.StringsRepository
import javax.inject.Inject

class ValidateName @Inject constructor(
    private val stringsRepository: StringsRepository
) {

    operator fun invoke(name: String): ValidationResult =
        if (name.isBlank()) ValidationResult(
            successful = false,
            errorMessage = stringsRepository.blankFullNameMessage
        ) else ValidationResult(successful = true)

}