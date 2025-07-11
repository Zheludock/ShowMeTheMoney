package com.example.data.retrofit

import com.example.data.dto.EmptyResponse
import com.example.data.dto.account.AccountDetailsResponse
import com.example.data.dto.account.AccountHistoryResponse
import com.example.data.dto.account.AccountResponse
import com.example.data.dto.account.CreateAccountRequest
import com.example.data.dto.account.UpdateAccountRequest
import dagger.Provides
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
/**
 * Retrofit-интерфейс для работы с API банковских счетов.
 * Определяет все доступные сетевые запросы для управления счетами.
 */
interface AccountApiService {
     /**
     * Получает список всех счетов пользователя.
     * @return Список счетов в формате [AccountResponse]
     */
    @GET("accounts")
    suspend fun getAccounts(): List<AccountResponse>
    /**
     * Создает новый банковский счет.
     * @param request Данные для создания счета [CreateAccountRequest]
     * @return Созданный счет [AccountResponse]
     */
    @POST("accounts")
    suspend fun createAccount(
        @Body request: CreateAccountRequest
    ): AccountResponse
    /**
     * Получает детальную информацию о счете.
     * @param accountId ID запрашиваемого счета
     * @return Детали счета [AccountDetailsResponse]
     */
    @GET("accounts/{id}")
    suspend fun getAccountDetails(
        @Path("id") accountId: Int
    ): AccountDetailsResponse
    /**
     * Обновляет данные счета.
     * @param accountId ID обновляемого счета
     * @param request Новые данные счета [UpdateAccountRequest]
     * @return Обновленный счет [AccountResponse]
     */
    @PUT("accounts/{id}")
    suspend fun updateAccount(
        @Path("id") accountId: Int,
        @Body request: UpdateAccountRequest
    ): AccountResponse
    /**
     * Удаляет счет.
     * @param accountId ID удаляемого счета
     * @return Пустой ответ с кодом статуса
     */
    @DELETE("accounts/{id}")
    suspend fun deleteAccount(
        @Path("id") accountId: Int
    ): Response<EmptyResponse>
    /**
     * Получает историю операций по счету.
     * @param accountId ID счета
     * @return История операций [AccountHistoryResponse]
     */
    @GET("accounts/{id}/history")
    suspend fun getAccountHistory(
        @Path("id") accountId: Int
    ): AccountHistoryResponse
}