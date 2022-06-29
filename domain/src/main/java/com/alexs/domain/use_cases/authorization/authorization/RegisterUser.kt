package com.alexs.domain.use_cases.authorization.authorization

import com.alexs.domain.model.Resource
import com.alexs.domain.model.ResponseValues
import com.alexs.domain.repository.UserAccountRepository
import com.alexs.domain.repository.UserDataStoreRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterUser @Inject constructor(
    private val userAccountRepository: UserAccountRepository,
    private val userDataStoreRepository: UserDataStoreRepository
) {
    operator fun invoke(userName: String, userPassword: String) = flow {
        emit(Resource.Loading)
        when (val registrationResult = userAccountRepository.registerUser(userName, userPassword)) {
            is ResponseValues.Failure -> emit(Resource.Error(registrationResult.error.errorMessage))
            is ResponseValues.Success -> {
                val name = registrationResult.data
                when (val loginResult = userAccountRepository.loginUser(name, userPassword)) {
                    is ResponseValues.Failure -> emit(Resource.Error(loginResult.error.errorMessage))
                    is ResponseValues.Success -> {
                        emit(Resource.Success(data = true))
                        userDataStoreRepository.persistAuthToken(loginResult.data)
                    }
                }
            }
        }
    }
}