package com.alexs.remote.api.dto.post

import com.google.gson.annotations.SerializedName

data class AuthorizationBody(
    @SerializedName("name")
    val userName: String,
    @SerializedName("password")
    val password: String
)
