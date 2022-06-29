package com.alexs.domain.use_cases.authorization.validation

import com.alexs.domain.model.ValidationResult
import com.alexs.domain.repository.StringsRepository
import java.util.regex.Pattern
import javax.inject.Inject

class ValidatePhoneNumber @Inject constructor(
    private val stringsRepository: StringsRepository
) {

    companion object {
        private const val PHONE_PATTERN =
            "^([+]?[\\s0-9]+)?(\\d{3}|[(]?[0-9]+[)])?([-]?[\\s]?[0-9])+\$"
    }

    operator fun invoke(phoneNumber: String): ValidationResult {
        val phone = phoneNumber.filter { it.isDigit() }
        if (phone.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = stringsRepository.blankPhoneMessage
            )
        }
        if (!Pattern.compile(PHONE_PATTERN, Pattern.CASE_INSENSITIVE)
                .matcher(phone)
                .matches()
        ) {
            return ValidationResult(
                successful = false,
                errorMessage = stringsRepository.invalidPhone
            )
        }
        return ValidationResult(successful = true)
    }

}