package com.alexs.authorization.model

sealed class LoginFormEvent {
    data class NameChanged(val name: String) : LoginFormEvent()
    data class PasswordChanged(val password: String) : LoginFormEvent()
    object RegistrationSelected : LoginFormEvent()
    object Submit : LoginFormEvent()
}