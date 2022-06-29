package com.alexs.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexs.authorization.model.AuthenticationFormState
import com.alexs.authorization.model.LoginFormEvent
import com.alexs.authorization.model.RegistrationFormEvent
import com.alexs.domain.model.Resource
import com.alexs.domain.use_cases.authorization.common.AuthorizationCases
import com.alexs.domain.use_cases.authorization.common.ValidationCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val validationCases: ValidationCases,
    private val authorizationCases: AuthorizationCases
) : ViewModel() {

    private val _authorizationEventChannel =
        MutableStateFlow<AuthorizationEvent>(AuthorizationEvent.Idle)
    val authorizationEventChannel = _authorizationEventChannel.asStateFlow()

    private val _selectedPage = MutableStateFlow(value = 0)
    val selectedPage = _selectedPage.asStateFlow()

    private val _registrationState = MutableStateFlow(AuthenticationFormState())
    val registrationState = _registrationState.asStateFlow()

    private val _loginState = MutableStateFlow(AuthenticationFormState())
    val loginState = _loginState.asStateFlow()

    fun onRegistrationFormEvent(event: RegistrationFormEvent) {
        when (event) {
            is RegistrationFormEvent.NameChanged -> {
                _registrationState.value = _registrationState.value.copy(fullName = event.name)
            }
            is RegistrationFormEvent.EmailChanged -> {
                _registrationState.value = _registrationState.value.copy(email = event.email)
            }
            is RegistrationFormEvent.PhoneChanged -> {
                _registrationState.value = _registrationState.value.copy(phoneNumber = event.phone)
            }
            is RegistrationFormEvent.PasswordChanged -> {
                _registrationState.value = _registrationState.value.copy(password = event.password)
            }
            is RegistrationFormEvent.RepeatedPasswordChanged -> {
                _registrationState.value =
                    _registrationState.value.copy(repeatedPassword = event.repeatedPassword)
            }
            is RegistrationFormEvent.TernsAgreed -> _registrationState.value =
                _registrationState.value.copy(ternsAgreed = event.agreed)
            RegistrationFormEvent.LoginSelected -> _selectedPage.value = 1
            RegistrationFormEvent.Submit -> submitRegistrationData()
        }
    }

    fun onLoginFormEvent(event: LoginFormEvent) {
        when (event) {
            is LoginFormEvent.NameChanged -> {
                _loginState.value = _loginState.value.copy(fullName = event.name)
            }
            is LoginFormEvent.PasswordChanged -> {
                _loginState.value = _loginState.value.copy(password = event.password)
            }
            LoginFormEvent.RegistrationSelected -> _selectedPage.value = 0
            LoginFormEvent.Submit -> submitLoginData()
        }
    }

    private fun submitRegistrationData() = viewModelScope.launch(Dispatchers.IO) {
        _authorizationEventChannel.value = AuthorizationEvent.Loading

        val nameResult = validationCases.validateFullName(_registrationState.value.fullName)
        val emailResult = validationCases.validateEmail(_registrationState.value.email)
        val phoneResult = validationCases.validatePhoneNumber(_registrationState.value.phoneNumber)
        val passwordResult = validationCases.validatePassword(_registrationState.value.password)
        val repeatedPasswordResult = validationCases.validateRepeatedPassword(
            _registrationState.value.password,
            _registrationState.value.repeatedPassword
        )

        _registrationState.value = _registrationState.value.copy(
            fullNameError = nameResult.errorMessage,
            emailError = emailResult.errorMessage,
            phoneNumberError = phoneResult.errorMessage,
            passwordError = passwordResult.errorMessage,
            repeatedPasswordError = repeatedPasswordResult.errorMessage
        )

        val hasError = listOf(
            nameResult,
            emailResult,
            passwordResult,
            repeatedPasswordResult,
            phoneResult
        ).any { !it.successful }

        if (hasError) {
            _authorizationEventChannel.value = AuthorizationEvent.Idle
            return@launch
        }
        registerUser()
    }

    private fun submitLoginData() = viewModelScope.launch(Dispatchers.IO) {
        _authorizationEventChannel.value = AuthorizationEvent.Loading

        val nameResult = validationCases.validateFullName(_loginState.value.fullName)
        val passwordResult = validationCases.validatePassword(_loginState.value.password)

        _loginState.value = _loginState.value.copy(
            fullNameError = nameResult.errorMessage,
            passwordError = passwordResult.errorMessage
        )

        val hasError = listOf(nameResult, passwordResult).any { !it.successful }
        if (hasError) {
            _authorizationEventChannel.value = AuthorizationEvent.Idle
            return@launch
        }
        loginUser()
    }

    private fun registerUser() = authorizationCases.registerUser(
        userName = _registrationState.value.fullName,
        userPassword = _registrationState.value.password
    ).onEach { result ->
        when (result) {
            is Resource.Error -> _authorizationEventChannel.value =
                AuthorizationEvent.Error(result.error)
            is Resource.Success -> {
                saveUserInfo(
                    userName = _registrationState.value.fullName,
                    userEmail = _registrationState.value.email,
                    userPhone = _registrationState.value.phoneNumber
                )
                _authorizationEventChannel.value = AuthorizationEvent.Success
            }
            else -> _authorizationEventChannel.value = AuthorizationEvent.Idle
        }
    }
        .catch {
            Timber.e(it)
        }
        .flowOn(Dispatchers.IO)
        .launchIn(viewModelScope)

    private fun loginUser() = authorizationCases.loginUser(
        userName = _loginState.value.fullName,
        userPassword = _loginState.value.password
    ).onEach { result ->
        when (result) {
            is Resource.Error -> _authorizationEventChannel.value =
                AuthorizationEvent.Error(result.error)
            is Resource.Success -> {
                saveUserInfo(
                    userName = _loginState.value.fullName,
                    userEmail = _loginState.value.email,
                    userPhone = _loginState.value.phoneNumber
                )
                _authorizationEventChannel.value = AuthorizationEvent.Success
            }
            else -> _authorizationEventChannel.value = AuthorizationEvent.Idle
        }
    }
        .catch {
            Timber.e(it)
        }
        .flowOn(Dispatchers.IO)
        .launchIn(viewModelScope)

    private fun saveUserInfo(userName: String, userEmail: String, userPhone: String) =
        authorizationCases.saveUserData(
            scope = viewModelScope,
            userName = userName,
            userEmail = userEmail,
            userPhone = userPhone
        )

    sealed class AuthorizationEvent {
        object Success : AuthorizationEvent()
        object Loading : AuthorizationEvent()
        object Idle : AuthorizationEvent()
        data class Error(val error: String) : AuthorizationEvent()
    }

}