package com.example.data.repository

import com.example.data.dto.transaction.CreateTransactionRequest
import com.example.data.dto.transaction.toDomain
import com.example.data.dto.transaction.toEntity
import com.example.data.retrofit.TransactionApiService
import com.example.data.room.dao.TransactionDao
import com.example.data.safecaller.ApiCallHelper
import com.example.domain.model.CreateTransactionDomain
import com.example.domain.model.TransactionDomain
import com.example.domain.model.TransactionInput
import com.example.domain.repository.TransactionRepository
import com.example.domain.response.ApiResult
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
    private val apiCallHelper: ApiCallHelper,
    private val transactionDao: TransactionDao
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
        val cached = transactionDao.getTransactions(accountId, startDate, endDate)
        if (cached.isNotEmpty()) {
            return ApiResult.Success(cached.map { it.toDomain() })
        }

        return apiCallHelper.safeApiCall(block = {
            val remote = apiService.getTransactions(accountId, startDate, endDate)
            transactionDao.insertTransactions(remote.map { it.toEntity() })
            remote.map { it.toDomain() }
        })
    }

    override suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): ApiResult<CreateTransactionDomain> {
        return apiCallHelper.safeApiCall(block = {
            val request = CreateTransactionRequest(
                accountId = accountId,
                categoryId = categoryId,
                amount = amount,
                transactionDate = transactionDate,
                comment = comment
            )
            val response = apiService.createTransaction(request)
            // Сохраняем в БД
            transactionDao.insertTransaction(response.toEntity())
            response.toDomain()
        })
    }

    override suspend fun getTransactionDetails(transactionId: Int): ApiResult<TransactionDomain> {
        return apiCallHelper.safeApiCall(block = {
            val cached = transactionDao.getTransactionWithDetails(transactionId)
            if (cached != null) {
                cached.toDomain()
            } else {
                val remote = apiService.getTransactionDetails(transactionId)
                transactionDao.insertTransaction(remote.toEntity())
                remote.toDomain()
            }
        })
    }

    override suspend fun updateTransaction(transactionInput: TransactionInput): ApiResult<TransactionDomain> {
        return apiCallHelper.safeApiCall(block = {
            val request = CreateTransactionRequest(
                accountId = transactionInput.accountId,
                categoryId = transactionInput.categoryId,
                amount = transactionInput.amount,
                transactionDate = transactionInput.transactionDate,
                comment = transactionInput.comment
            )
            val updated = apiService.updateTransaction(transactionInput.transactionId, request)
            transactionDao.updateTransaction(updated.toEntity())
            updated.toDomain()
        })
    }

    override suspend fun deleteTransaction(transactionId: Int): ApiResult<Boolean> {
        return apiCallHelper.safeApiCall(block = {
            val response = apiService.deleteTransaction(transactionId)
            if (response.isSuccessful) {
                transactionDao.deleteTransaction(transactionId)
            }
            response.isSuccessful
        })
    }
}

