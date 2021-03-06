package com.alexs.remote.api.dto.get

import com.google.gson.annotations.SerializedName

data class LoanResponse(
    @SerializedName("amount")
    val amount: Int,
    @SerializedName("date")
    val date: String,
    @SerializedName("firstName")
    val firstName: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lastName")
    val lastName: String,
    @SerializedName("percent")
    val percent: Double,
    @SerializedName("period")
    val period: Int,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("state")
    val state: String
)