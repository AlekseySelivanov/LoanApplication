package com.alexs.authorization.model

sealed class RegistrationFormEvent {
    data class NameChanged(val name: String) : RegistrationFormEvent()
    data class EmailChanged(val email: String) : RegistrationFormEvent()
    data class PhoneChanged(val phone: String) : RegistrationFormEvent()
    data class PasswordChanged(val password: String) : RegistrationFormEvent()
    data class RepeatedPasswordChanged(val repeatedPassword: String) : RegistrationFormEvent()
    data class TernsAgreed(val agreed: Boolean) : RegistrationFormEvent()
    object LoginSelected : RegistrationFormEvent()
    object Submit : RegistrationFormEvent()
}
