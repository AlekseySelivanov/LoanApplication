package com.alexs.domain.use_cases.authorization.common

import com.alexs.domain.use_cases.authorization.authorization.LoginUser
import com.alexs.domain.use_cases.authorization.authorization.RegisterUser
import com.alexs.domain.use_cases.authorization.user_data.SaveUserData
import javax.inject.Inject

data class AuthorizationCases @Inject constructor(
    val registerUser: RegisterUser,
    val loginUser: LoginUser,
    val saveUserData: SaveUserData
)
