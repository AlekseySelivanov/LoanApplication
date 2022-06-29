package com.alexs.domain.use_cases.authorization.user_data

import com.alexs.domain.model.Resource
import com.alexs.domain.repository.UserDataStoreRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReadAuthToken @Inject constructor(
    private val userDataStoreRepository: UserDataStoreRepository
) {

    operator fun invoke() = userDataStoreRepository.readAuthToken.map { token ->
        token?.let { Resource.Success(data = token) } ?: Resource.Error("")
    }

}