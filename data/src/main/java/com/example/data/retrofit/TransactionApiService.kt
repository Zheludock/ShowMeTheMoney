package com.example.data.retrofit

import com.example.data.dto.transaction.CreateTransactionRequest
import com.example.data.dto.transaction.TransactionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
/**
 * Retrofit-интерфейс для работы с API транзакций.
 * Определяет все доступные сетевые запросы для работы с транзакциями.
 */
interface TransactionApiService {
    /**
     * Получает список транзакций для указанного аккаунта.
     *
     * @param accountId Идентификатор аккаунта
     * @param startDate Начальная дата периода
     * @param endDate Конечная дата периода
     * @return Список транзакций в формате [TransactionResponse]
     */
    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactions(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): List<TransactionResponse>
    /**
     * Создает новую транзакцию.
     *
     * @param request Данные для создания транзакции [CreateTransactionRequest]
     * @return Созданная транзакция [TransactionResponse]
     */
    @POST("transactions")
    suspend fun createTransaction(
        @Body request: CreateTransactionRequest
    ): TransactionResponse
    /**
     * Получает детали конкретной транзакции.
     *
     * @param transactionId Идентификатор транзакции
     * @return Детали транзакции [TransactionResponse]
     */
    @GET("transactions/{id}")
    suspend fun getTransactionDetails(
        @Path("id") transactionId: Int
    ): TransactionResponse
    /**
     * Обновляет существующую транзакцию.
     *
     * @param transactionId Идентификатор транзакции
     * @param request Новые данные транзакции [CreateTransactionRequest]
     * @return Обновленная транзакция [TransactionResponse]
     */
    @PUT("transactions/{id}")
    suspend fun updateTransaction(
        @Path("id") transactionId: Int,
        @Body request: CreateTransactionRequest
    ): TransactionResponse
    /**
     * Удаляет транзакцию.
     *
     * @param transactionId Идентификатор транзакции
     * @return Пустой ответ с кодом статуса
     */
    @DELETE("transactions/{id}")
    suspend fun deleteTransaction(
        @Path("id") transactionId: Int
    ): Response<Unit>
}