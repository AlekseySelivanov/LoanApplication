package com.alexs.loan_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.alexs.domain.model.Resource
import com.alexs.domain.model.items.LoanConditions
import com.alexs.domain.model.items.LoanItem
import com.alexs.domain.use_cases.authorization.common.LoanDetailsCases
import com.alexs.loan_details.model.LoanFormEvent
import com.alexs.loan_details.model.LoanFormState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoanViewModel constructor(
    private val loanId: Int,
    private val loanDetailsCases: LoanDetailsCases
) : ViewModel() {

    private val _loanRequestEventChannel =
        MutableStateFlow<LoanRequestEvent>(LoanRequestEvent.Loading)
    val loanRequestEventChannel = _loanRequestEventChannel.asStateFlow()

    private val _loanFormState = MutableStateFlow(LoanFormState())
    val loanFormState = _loanFormState.asStateFlow()

    private val _loanConditions = MutableStateFlow(LoanConditions())
    val loanConditions = _loanConditions.asStateFlow()

    private var authToken: String = ""

    init {
        loanAuthToken()
    }

    private fun loanAuthToken() = loanDetailsCases.readAuthToken().onEach { resource ->
        when (resource) {
            is Resource.Success -> {
                authToken = resource.data

                if (loanId == -1) loadLoanConditions() else loadLoan()
                loadUserData()
            }
            else -> Unit
        }
    }
        .flowOn(Dispatchers.IO)
        .launchIn(viewModelScope)

    private fun loadLoan() = loanDetailsCases.getLoan(authToken, loanId).onEach { resource ->
        when (resource) {
            is Resource.Error -> _loanRequestEventChannel.value =
                LoanRequestEvent.Error(resource.error)
            Resource.Idle -> _loanRequestEventChannel.value = LoanRequestEvent.Idle
            Resource.Loading -> _loanRequestEventChannel.value = LoanRequestEvent.Loading
            is Resource.Success -> _loanRequestEventChannel.value =
                LoanRequestEvent.LoanLoaded(
                    resource.data
                )
        }
    }
        .flowOn(Dispatchers.IO)
        .launchIn(viewModelScope)

    private fun loadUserData() = viewModelScope.launch(Dispatchers.IO) {
        val userData = loanDetailsCases.readUserData()
        _loanFormState.value = _loanFormState.value.copy(
            firstName = userData.firstName,
            lastName = userData.lastName,
            phoneNumber = userData.phoneNumber
        )
    }

    private suspend fun loadLoanConditions() {
        when (val conditions = loanDetailsCases.getLoanConditions(authToken)) {
            is Resource.Error ->
                _loanRequestEventChannel.value = LoanRequestEvent.Error(conditions.error)
            Resource.Loading -> _loanRequestEventChannel.value = LoanRequestEvent.Loading
            is Resource.Success -> {
                _loanConditions.value = conditions.data
                _loanRequestEventChannel.value = LoanRequestEvent.Idle
            }
            else -> Unit
        }
    }

    fun onLoanFormEvent(event: LoanFormEvent) {
        when (event) {
            is LoanFormEvent.FirstNameChanged -> {
                _loanFormState.value = _loanFormState.value.copy(firstName = event.firstName)
            }
            is LoanFormEvent.LastNameChanged -> {
                _loanFormState.value = _loanFormState.value.copy(lastName = event.lastName)
            }
            is LoanFormEvent.PhoneChanged -> {
                _loanFormState.value = _loanFormState.value.copy(phoneNumber = event.phoneNumber)
            }
            is LoanFormEvent.AmountChanged -> {
                _loanFormState.value = _loanFormState.value.copy(loanAmount = event.amount)
            }
            LoanFormEvent.Apply -> applyLoanRequest()
        }
    }

    private fun applyLoanRequest() = viewModelScope.launch(Dispatchers.IO) {
        _loanRequestEventChannel.value = LoanRequestEvent.Loading

        val firstNameResult = loanDetailsCases.validateFirstName(_loanFormState.value.firstName)
        val lastNameResult = loanDetailsCases.validateLastName(_loanFormState.value.lastName)
        val phoneResult = loanDetailsCases.validatePhoneNumber(_loanFormState.value.phoneNumber)
        val amountResult = loanDetailsCases.validateAmount(
            _loanFormState.value.loanAmount,
            loanConditions.value.maxAmount.toString()
        )

        _loanFormState.value = _loanFormState.value.copy(
            firstNameError = firstNameResult.errorMessage,
            lastNameError = lastNameResult.errorMessage,
            phoneNumberError = phoneResult.errorMessage,
            loanAmountError = amountResult.errorMessage
        )

        val hasError = listOf(
            firstNameResult,
            lastNameResult,
            phoneResult,
            amountResult
        ).any { !it.successful }

        if (hasError) {
            _loanRequestEventChannel.value = LoanRequestEvent.Idle
            return@launch
        }
        createLoanRequest()
    }

    private fun createLoanRequest() = with(_loanFormState.value) {
        loanDetailsCases.applyLoan(
            authToken,
            amount = loanAmount.toInt(),
            firstName = firstName,
            lastName = lastName,
            percent = _loanConditions.value.percent,
            period = _loanConditions.value.period,
            phoneNumber = phoneNumber
        ).onEach { resource ->
            when (resource) {
                is Resource.Error ->
                    _loanRequestEventChannel.value = LoanRequestEvent.Error(resource.error)
                Resource.Idle -> _loanRequestEventChannel.value = LoanRequestEvent.Idle
                Resource.Loading -> _loanRequestEventChannel.value = LoanRequestEvent.Loading
                is Resource.Success -> _loanRequestEventChannel.value = LoanRequestEvent.Success
            }
        }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    sealed class LoanRequestEvent {
        object Success : LoanRequestEvent()
        data class LoanLoaded(val data: LoanItem) : LoanRequestEvent()
        object Loading : LoanRequestEvent()
        object Idle : LoanRequestEvent()
        data class Error(val message: String) : LoanRequestEvent()
    }

    @Suppress("UNCHECKED_CAST")
    class LoanViewModelFactory @AssistedInject constructor(
        @Assisted(value = "loanId") private val loanId: Int,
        private val loanDetailsCases: LoanDetailsCases
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoanViewModel(
                loanId = loanId,
                loanDetailsCases = loanDetailsCases
            ) as T
        }

        @AssistedFactory
        interface Factory {
            fun create(@Assisted(value = "loanId") loanId: Int): LoanViewModelFactory
        }

    }

}