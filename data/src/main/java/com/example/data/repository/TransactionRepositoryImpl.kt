package com.example.data.repository

import android.util.Log
import com.example.data.dto.transaction.CreateTransactionRequest
import com.example.data.dto.transaction.toDomain
import com.example.data.dto.transaction.toEntity
import com.example.data.retrofit.TransactionApiService
import com.example.data.room.dao.TransactionDao
import com.example.data.room.entityes.TransactionEntity
import com.example.data.safecaller.ApiCallHelper
import com.example.domain.model.TransactionDomain
import com.example.domain.model.TransactionInput
import com.example.domain.repository.TransactionRepository
import com.example.domain.response.ApiResult
import com.example.utils.AccountManager
import com.example.utils.DateUtils
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
    ): List<TransactionDomain> {
        val start = DateUtils.formatDateToIso8601startDay(startDate)
        val end = DateUtils.formatDateToEndOfDayIso8601(endDate)
        val count = transactionDao.transactionsCountByAccount(AccountManager.selectedAccountId)

        if(count > 0){
            Log.d("Room", "Запрос в ROOM с параметрами $startDate, $endDate")
            val cached = transactionDao.getTransactions(accountId,
                start,
                end)
            return cached.filter { !it.transaction.isDeleted }.map { it.toDomain() }
        }

        val apiResult = apiCallHelper.safeApiCall({ apiService.getTransactions(accountId, "2025-06-01",
            DateUtils.formatCurrentDate()) })
        return when (apiResult) {
            is ApiResult.Success -> {
                transactionDao.insertTransactions(apiResult.data.map { it.toEntity() })
                Log.d("Room", "Данные загружены в Room")
                transactionDao.getTransactions(accountId,
                    start,
                    end).map { it.toDomain() }
            }
            else -> emptyList()
        }
    }

    override suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ) {
        val newTransaction = TransactionEntity(
            id = 0,
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            comment = comment,
            transactionDate = transactionDate,
            createdAt = DateUtils.formatCurrentDate(),
            updatedAt = null,
            pendingSync = true,
            isDeleted = false
        )
        transactionDao.insertTransaction(newTransaction)
    }

    override suspend fun getTransactionDetails(transactionId: Int): TransactionDomain {
        val cached = transactionDao.getTransactionWithDetails(transactionId)
        if (cached != null) {
            if (cached.transaction.isDeleted) {
                throw Exception("Transaction $transactionId has been deleted")
            }
            return cached.toDomain()
        }

        val apiResult = apiCallHelper.safeApiCall({ apiService.getTransactionDetails(transactionId) })
        return when (apiResult) {
            is ApiResult.Success -> {
                val transaction = apiResult.data
                transactionDao.insertTransaction(transaction.toEntity())
                transaction.toDomain()
            }
            else -> {
                throw Exception("Failed to fetch transaction $transactionId: $apiResult")
            }
        }
    }

    override suspend fun updateTransaction(transactionInput: TransactionInput) {
        val updatedEntity = transactionDao.getTransactionById(transactionInput.transactionId)
        updatedEntity.copy(
            categoryId = transactionInput.categoryId,
            amount = transactionInput.amount,
            transactionDate = transactionInput.transactionDate,
            comment = transactionInput.comment,
            pendingSync = true,
        )
        transactionDao.updateTransaction(updatedEntity)
    }

    override suspend fun deleteTransaction(transactionId: Int): Boolean {
        transactionDao.markTransactionDeleted(transactionId)
        return transactionDao.getTransactionById(transactionId).isDeleted
    }

    suspend fun syncPendingTransactions() {
        val pending = transactionDao.getPendingSyncTransactions()

        pending.forEach { transaction ->
            try {
                if (transaction.isDeleted) {
                    val response = apiService.deleteTransaction(transaction.id)
                    if (response.isSuccessful) {
                        transactionDao.deleteTransaction(transaction.id)
                    }
                } else if (transaction.id < 0) {
                    val request = CreateTransactionRequest(
                        accountId = transaction.accountId,
                        categoryId = transaction.categoryId,
                        amount = transaction.amount,
                        transactionDate = transaction.transactionDate,
                        comment = transaction.comment
                    )
                    val response = apiCallHelper.safeApiCall({ apiService.createTransaction(request) })
                    transactionDao.deleteTransaction(transaction.id)
                    when (response){
                        is ApiResult.Success -> transactionDao.insertTransaction(response.data.toEntity())
                        else -> {}
                    }

                } else {
                    val request = CreateTransactionRequest(
                        accountId = transaction.accountId,
                        categoryId = transaction.categoryId,
                        amount = transaction.amount,
                        transactionDate = transaction.transactionDate,
                        comment = transaction.comment
                    )
                    val updated = apiService.updateTransaction(transaction.id, request)
                    transactionDao.updateTransaction(updated.toEntity())
                }
            } catch (e: Exception) {
                // Игнорируем ошибки, синхронизация повторится позже
            }
        }
    }
}

