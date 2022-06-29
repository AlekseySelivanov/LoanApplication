package com.alexs.remote.api.dto.get

import com.google.gson.annotations.SerializedName

data class AuthorizationResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("role")
    val role: String
)
