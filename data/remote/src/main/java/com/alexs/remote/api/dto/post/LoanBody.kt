package com.alexs.remote.api.dto.post

import com.google.gson.annotations.SerializedName

data class LoanBody(
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("percent")
    val percent: Double,
    @SerializedName("period")
    val period: Int,
    @SerializedName("phoneNumber")
    val phoneNumber: String
)
