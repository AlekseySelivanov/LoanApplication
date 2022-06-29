package com.alexs.domain.repository

import com.alexs.domain.model.ResponseValues
import kotlinx.coroutines.flow.Flow

interface UserAccountRepository {
    suspend fun registerUser(userName: String, userPassword: String): ResponseValues<String>
    suspend fun loginUser(userName: String, userPassword: String): ResponseValues<String>
}