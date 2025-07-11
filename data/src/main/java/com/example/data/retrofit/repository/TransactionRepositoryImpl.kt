package com.example.data.retrofit.repository

import com.example.data.retrofit.TransactionApiService
import com.example.data.dto.transaction.CreateTransactionRequest
import com.example.data.dto.transaction.toDomain
import com.example.data.safecaller.ApiCallHelper
import com.example.domain.response.ApiResult
import com.example.domain.model.TransactionDomain
import com.example.domain.model.TransactionInput
import com.example.domain.repository.TransactionRepository
import javax.inject.Inject
/**
 * Реализация [TransactionRepository] для работы с транзакциями через сетевой API.
 * Обрабатывает все операции с транзакциями и преобразует данные между доменной моделью и API.
 *
 * @param apiService Сетевой API для работы с транзакциями
 * @param apiCallHelper Инструмент для безопасных вызовов API с обработкой ошибок. Все обращения
 *                                                  к сети выполнять только внутри apiCallHelper!
 */
class TransactionRepositoryImpl @Inject constructor(
    private val apiService: TransactionApiService,
    private val apiCallHelper: ApiCallHelper
) : TransactionRepository {
    /**
     * Получает список транзакций с возможностью фильтрации по дате.
     * @param accountId Идентификатор аккаунта
     * @param startDate Начальная дата периода
     * @param endDate Конечная дата периода
     * @return [ApiResult] с списком [TransactionDomain] или ошибкой
     */
    override suspend fun getTransactions(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): ApiResult<List<TransactionDomain>> {
        return apiCallHelper.safeApiCall(block =
            {apiService.getTransactions(accountId, startDate, endDate).map { it.toDomain() }}
        )
    }
    /**
     * Создает новую транзакцию.
     * @param accountId Идентификатор аккаунта
     * @param categoryId Идентификатор категории
     * @param amount Сумма транзакции
     * @param transactionDate Дата транзакции
     * @param comment Комментарий к транзакции (опционально)
     * @return [ApiResult] с созданной [TransactionDomain] или ошибкой
     */
    override suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): ApiResult<TransactionDomain> {
        val request = CreateTransactionRequest(
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            transactionDate = transactionDate,
            comment = comment
        )
        return apiCallHelper.safeApiCall(block = {
            apiService.createTransaction(request).toDomain()
        })
    }
    /**
     * Получает детали конкретной транзакции.
     * @param transactionId Идентификатор транзакции
     * @return [ApiResult] с [TransactionDomain] или ошибкой
     */
    override suspend fun getTransactionDetails(transactionId: Int): ApiResult<TransactionDomain> {
        return apiCallHelper.safeApiCall(block = {
            apiService.getTransactionDetails(transactionId).toDomain()
        })
    }
    /**
     * Обновляет существующую транзакцию.
     * @param transactionInput Данные для обновления транзакции
     * @return [ApiResult] с обновленной [TransactionDomain] или ошибкой
     */
    override suspend fun updateTransaction(transactionInput: TransactionInput): ApiResult<TransactionDomain> {
        val request = CreateTransactionRequest(
            accountId = transactionInput.accountId,
            categoryId = transactionInput.categoryId,
            amount = transactionInput.amount,
            transactionDate = transactionInput.transactionDate,
            comment = transactionInput.comment
        )
        return apiCallHelper.safeApiCall(block = {
            apiService.updateTransaction(transactionInput.transactionId, request).toDomain()
        })
    }
    /**
     * Удаляет транзакцию.
     * @param transactionId Идентификатор транзакции
     * @return [ApiResult] с результатом операции (true/false) или ошибкой
     */
    override suspend fun deleteTransaction(transactionId: Int): ApiResult<Boolean> {
        val response = apiService.deleteTransaction(transactionId)
        return apiCallHelper.safeApiCall(block = {
            response.isSuccessful
        })
    }
}