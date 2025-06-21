package com.example.showmethemoney.data

import com.example.showmethemoney.data.dto.EmptyResponse
import com.example.showmethemoney.data.dto.account.AccountDetailsResponse
import com.example.showmethemoney.data.dto.account.AccountHistoryResponse
import com.example.showmethemoney.data.dto.account.AccountResponse
import com.example.showmethemoney.data.dto.account.CreateAccountRequest
import com.example.showmethemoney.data.dto.account.UpdateAccountRequest
import com.example.showmethemoney.data.dto.category.CategoryResponse
import com.example.showmethemoney.data.dto.transaction.CreateTransactionRequest
import com.example.showmethemoney.data.dto.transaction.TransactionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface FinanceApiService {

    @GET("accounts")
    suspend fun getAccounts(): List<AccountResponse>

    @POST("accounts")
    suspend fun createAccount(
        @Body request: CreateAccountRequest
    ): AccountResponse

    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactions(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): List<TransactionResponse>

    @GET("accounts/{id}")
    suspend fun getAccountDetails(
        @Path("id") accountId: Int
    ): AccountDetailsResponse

    @PUT("accounts/{id}")
    suspend fun updateAccount(
        @Path("id") accountId: Int,
        @Body request: UpdateAccountRequest
    ): AccountResponse

    @DELETE("accounts/{id}")
    suspend fun deleteAccount(
        @Path("id") accountId: Int
    ): Response<EmptyResponse>

    @GET("accounts/{id}/history")
    suspend fun getAccountHistory(
        @Path("id") accountId: Int
    ): AccountHistoryResponse

    @GET("categories")
    suspend fun getAllCategories(): List<
            CategoryResponse>

    @GET("categories/type/{isIncome}")
    suspend fun getCategoriesByType(
        @Path("isIncome") isIncome: Boolean
    ): List<CategoryResponse>

    @POST("transactions")
    suspend fun createTransaction(
        @Body request: CreateTransactionRequest
    ): TransactionResponse

    @GET("transactions/{id}")
    suspend fun getTransactionDetails(
        @Path("id") transactionId: Int
    ): TransactionResponse

    @PUT("transactions/{id}")
    suspend fun updateTransaction(
        @Path("id") transactionId: Int,
        @Body request: CreateTransactionRequest
    ): TransactionResponse

    @DELETE("transactions/{id}")
    suspend fun deleteTransaction(
        @Path("id") transactionId: Int
    ): Response<Unit>
}