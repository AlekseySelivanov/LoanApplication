package com.alexs.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexs.domain.model.Resource
import com.alexs.domain.use_cases.authorization.user_data.ReadAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    readAuthToken: ReadAuthToken
) : ViewModel() {

    private val _isAuthorized = MutableStateFlow<Resource<String>>(Resource.Idle)
    val isAuthorized = _isAuthorized.asStateFlow()

    init {
        readAuthToken()
            .onEach { resource ->
                _isAuthorized.value = resource
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

}