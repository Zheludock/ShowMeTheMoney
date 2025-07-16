package com.example.data.repository

import com.example.data.dto.transaction.CreateTransactionRequest
import com.example.data.dto.transaction.toDomain
import com.example.data.dto.transaction.toEntity
import com.example.data.retrofit.TransactionApiService
import com.example.data.room.dao.TransactionDao
import com.example.data.room.entityes.TransactionEntity
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

    override suspend fun getTransactions(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): ApiResult<List<TransactionDomain>> {
        // Всегда читаем из локальной базы
        val cached = transactionDao.getTransactions(accountId, startDate, endDate)
        return ApiResult.Success(cached.filter { !it.isDeleted }.map { it.toDomain() })
    }

    override suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): ApiResult<CreateTransactionDomain> {
        // Создаём локально с pendingSync = true
        val newTransaction = TransactionEntity(
            id = 0, // временный id, например можно генерировать отрицательные id для локальных транзакций
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            transactionDate = transactionDate,
            comment = comment,
            createdAt = ,
            pendingSync = true,
            isDeleted = false
        )
        transactionDao.insertTransaction(newTransaction)
        // Возвращаем локальный результат (можно расширить для генерации временного id)
        return ApiResult.Success(newTransaction.toDomain())
    }

    override suspend fun getTransactionDetails(transactionId: Int): ApiResult<TransactionDomain> {
        val cached = transactionDao.getTransactionWithDetails(transactionId)
        return if (cached != null && !cached.isDeleted) {
            ApiResult.Success(cached.toDomain())
        } else {
            ApiResult.Error(Throwable("Transaction not found"))
        }
    }

    override suspend fun updateTransaction(transactionInput: TransactionInput): ApiResult<TransactionDomain> {
        // Обновляем локально и ставим pendingSync
        val updatedEntity = TransactionEntity(
            id = transactionInput.transactionId,
            accountId = transactionInput.accountId,
            categoryId = transactionInput.categoryId,
            amount = transactionInput.amount,
            transactionDate = transactionInput.transactionDate,
            comment = transactionInput.comment,
            pendingSync = true,
            isDeleted = false
        )
        transactionDao.updateTransaction(updatedEntity)
        return ApiResult.Success(updatedEntity.toDomain())
    }

    override suspend fun deleteTransaction(transactionId: Int): ApiResult<Boolean> {
        // Мягко удаляем локально и ставим pendingSync
        transactionDao.markTransactionDeleted(transactionId)
        return ApiResult.Success(true)
    }

    // Новый метод для синхронизации
    suspend fun syncPendingTransactions() {
        val pending = transactionDao.getPendingSyncTransactions()

        pending.forEach { transaction ->
            try {
                if (transaction.isDeleted) {
                    val response = apiService.deleteTransaction(transaction.id)
                    if (response.isSuccessful) {
                        transactionDao.deleteTransaction(transaction.id)
                    }
                } else if (transaction.transactionId < 0) {
                    // Локальная новая транзакция с временным id
                    val request = CreateTransactionRequest(
                        accountId = transaction.accountId,
                        categoryId = transaction.categoryId,
                        amount = transaction.amount,
                        transactionDate = transaction.transactionDate,
                        comment = transaction.comment
                    )
                    val response = apiService.createTransaction(request)
                    transactionDao.deleteTransaction(transaction.transactionId) // удаляем старую с временным id
                    transactionDao.insertTransaction(response.toEntity()) // вставляем с новым id с сервера
                } else {
                    // Обновление существующей
                    val request = CreateTransactionRequest(
                        accountId = transaction.accountId,
                        categoryId = transaction.categoryId,
                        amount = transaction.amount,
                        transactionDate = transaction.transactionDate,
                        comment = transaction.comment
                    )
                    val updated = apiService.updateTransaction(transaction.transactionId, request)
                    transactionDao.updateTransaction(updated.toEntity())
                }
            } catch (e: Exception) {
                // Игнорируем ошибки, синхронизация повторится позже
            }
        }
    }
}

