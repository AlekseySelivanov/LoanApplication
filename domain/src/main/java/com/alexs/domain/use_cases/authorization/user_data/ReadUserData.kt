package com.alexs.domain.use_cases.authorization.user_data

import com.alexs.domain.model.items.UserData
import com.alexs.domain.repository.UserDataStoreRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ReadUserData @Inject constructor(
    private val userDataStoreRepository: UserDataStoreRepository
) {

    suspend operator fun invoke(): UserData {
        val userNameArray = userDataStoreRepository.readUserName.first().split(" ")
        val userPhone = userDataStoreRepository.readUserPhone.first()

        return UserData(
            firstName = userNameArray[0],
            lastName = userNameArray[1],
            phoneNumber = userPhone
        )
    }

}