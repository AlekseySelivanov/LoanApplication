package com.alexs.domain.use_cases.authorization.common

import com.alexs.domain.use_cases.authorization.validation.*
import javax.inject.Inject

data class ValidationCases @Inject constructor(
    val validateFullName: ValidateFullName,
    val validateEmail: ValidateEmail,
    val validatePhoneNumber: ValidatePhoneNumber,
    val validatePassword: ValidatePassword,
    val validateRepeatedPassword: ValidateRepeatedPassword
)
