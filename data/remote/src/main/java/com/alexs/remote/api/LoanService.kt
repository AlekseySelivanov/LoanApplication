package com.alexs.remote.api

import com.alexs.remote.api.dto.get.AuthorizationResponse
import com.alexs.remote.api.dto.get.ConditionsResponse
import com.alexs.remote.api.dto.get.LoanResponse
import com.alexs.remote.api.dto.post.AuthorizationBody
import com.alexs.remote.api.dto.post.LoanBody
import retrofit2.Response
import retrofit2.http.*

interface LoanService {

    @POST(value = "registration")
    suspend fun register(@Body authorizationBody: AuthorizationBody): Response<AuthorizationResponse>

    @POST(value = "login")
    suspend fun login(@Body authorizationBody: AuthorizationBody): Response<String>

    @POST(value = "loans")
    suspend fun createLoan(
        @Header(value = "Authorization") token: String,
        @Body loanBody: LoanBody
    ): Response<LoanResponse>

    @GET(value = "loans/all")
    suspend fun getLoansList(
        @Header(value = "Authorization") token: String
    ): Response<List<LoanResponse>>

    @GET(value = "loans/{id}")
    suspend fun getLoan(
        @Header(value = "Authorization") token: String,
        @Path(value = "id") loanId: Int
    ): Response<LoanResponse>

    @GET(value = "loans/conditions")
    suspend fun getLoanConditions(
        @Header(value = "Authorization") token: String
    ): Response<ConditionsResponse>

}