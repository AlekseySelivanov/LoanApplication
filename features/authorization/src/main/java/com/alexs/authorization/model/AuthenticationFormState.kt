package com.alexs.authorization.model

data class AuthenticationFormState(
    val fullName: String = "",
    val fullNameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val phoneNumber: String = "",
    val phoneNumberError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val repeatedPassword: String = "",
    val repeatedPasswordError: String? = null,
    val ternsAgreed: Boolean = false
)
