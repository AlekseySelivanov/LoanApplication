package com.alexs.domain.use_cases.authorization.authorization

import com.alexs.domain.model.Resource
import com.alexs.domain.model.ResponseValues
import com.alexs.domain.repository.UserAccountRepository
import com.alexs.domain.repository.UserDataStoreRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUser @Inject constructor(
    private val userAccountRepository: UserAccountRepository,
    private val userDataStoreRepository: UserDataStoreRepository
) {
    operator fun invoke(
        userName: String,
        userPassword: String
    ) = flow {
        when (val loginResult = userAccountRepository.loginUser(userName, userPassword)) {
            is ResponseValues.Failure -> emit(Resource.Error(loginResult.error.errorMessage))
            is ResponseValues.Success -> {
                emit(Resource.Success(data = true))
                userDataStoreRepository.persistAuthToken(
                    authToken = loginResult.data
                )
                userDataStoreRepository.persistUserName(userName)
            }
        }
    }

}