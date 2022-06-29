package com.alexs.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserDataStoreRepository {

    suspend fun persistUserName(userName: String)
    suspend fun persistAuthToken(authToken: String)
    suspend fun persistUserEmail(userEmail: String)
    suspend fun persistUserPhone(userPhone: String)

    val readUserName: Flow<String>
    val readAuthToken: Flow<String?>
    val readUserEmail: Flow<String>
    val readUserPhone: Flow<String>

}