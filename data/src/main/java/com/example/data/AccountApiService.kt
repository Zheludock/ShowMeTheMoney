package com.example.data

import com.example.data.dto.account.AccountDetailsResponse
import com.example.data.dto.account.AccountResponse
import com.example.data.dto.account.CreateAccountRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AccountApiService {

    @GET("accounts")
    suspend fun getAccounts(): List<AccountResponse>

    @POST("accounts")
    suspend fun createAccount(
        @Body request: CreateAccountRequest
    ): AccountResponse

    @GET("accounts/{id}")
    suspend fun getAccountDetails(
        @Path("id") accountId: Int
    ): AccountDetailsResponse

    @PUT("accounts/{id}")
    suspend fun updateAccount(
        @Path("id") accountId: Int,
        @Body request: com.example.data.dto.account.UpdateAccountRequest
    ): com.example.data.dto.account.AccountResponse

    @DELETE("accounts/{id}")
    suspend fun deleteAccount(
        @Path("id") accountId: Int
    ): Response<com.example.data.dto.EmptyResponse>

    @GET("accounts/{id}/history")
    suspend fun getAccountHistory(
        @Path("id") accountId: Int
    ): com.example.data.dto.account.AccountHistoryResponse


}