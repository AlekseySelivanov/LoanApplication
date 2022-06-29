package com.alexs.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexs.domain.model.Resource
import com.alexs.domain.model.items.LoanItem
import com.alexs.domain.use_cases.authorization.common.HomeCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeCases: HomeCases
) : ViewModel() {

    private var authToken: String = ""

    private val _loansList = MutableStateFlow<Resource<List<LoanItem>>>(Resource.Loading)
    val loansList = _loansList.asStateFlow()

    init {
        readAuthToken()
    }

    private fun readAuthToken() = homeCases.readAuthToken().onEach { resource ->
        when (resource) {
            is Resource.Success -> {
                authToken = resource.data
                readLoansList()
            }
            else -> Unit
        }
    }
        .flowOn(Dispatchers.IO)
        .launchIn(viewModelScope)

    private fun readLoansList() = homeCases.readLoansList().onEach { resource ->
        _loansList.value = resource
    }
        .catch {
            Timber.e(it)
        }
        .flowOn(Dispatchers.IO)
        .launchIn(viewModelScope)

    fun refreshLoansList() = viewModelScope.launch(Dispatchers.IO) {
        _loansList.value = Resource.Loading
        homeCases.fetchLoansList(authToken)
        _loansList.value = Resource.Idle
    }

}