package com.alexs.domain.use_cases.authorization.user_data

import com.alexs.domain.repository.UserDataStoreRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SaveUserData @Inject constructor(
    private val userDataStoreRepository: UserDataStoreRepository
) {
    operator fun invoke(
        scope: CoroutineScope,
        userName: String,
        userEmail: String,
        userPhone: String
    ) = scope.launch(Dispatchers.IO) {
        launch { userDataStoreRepository.persistUserName(userName = userName) }
        launch { userDataStoreRepository.persistUserEmail(userEmail = userEmail) }
        launch { userDataStoreRepository.persistUserPhone(userPhone = userPhone) }
    }

}