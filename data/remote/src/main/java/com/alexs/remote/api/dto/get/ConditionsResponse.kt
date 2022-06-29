package com.alexs.remote.api.dto.get

import com.google.gson.annotations.SerializedName

data class ConditionsResponse(
    @SerializedName("maxAmount")
    val maxAmount: Int,
    @SerializedName("percent")
    val percent: Double,
    @SerializedName("period")
    val period: Int
)