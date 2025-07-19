package com.example.data.repository

import android.util.Log
import com.example.data.dto.transaction.CreateTransactionRequest
import com.example.data.dto.transaction.TransactionResponse
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
        val temporaryId = generateTemporaryId()
        val newTransaction = TransactionEntity(
            id = temporaryId,
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
        Log.d("EDITTRANSACTION", "Repository получил запрос на обновление")
        val updatedEntity = transactionDao.getTransactionById(transactionInput.transactionId)
        Log.d("EDITTRANSACTION", "Создана entity: $updatedEntity")
        updatedEntity.copy(
            categoryId = transactionInput.categoryId,
            amount = transactionInput.amount,
            transactionDate = transactionInput.transactionDate,
            comment = transactionInput.comment,
            pendingSync = true,
        )
        transactionDao.updateTransaction(updatedEntity)
        Log.d("EDITTRANSACTION", "Запрос к БД выполнен")
    }

    override suspend fun deleteTransaction(transactionId: Int): Boolean {
        transactionDao.markTransactionDeleted(transactionId)
        return transactionDao.getTransactionById(transactionId).isDeleted
    }

    suspend fun syncPendingTransactions() {
        val pending = transactionDao.getPendingSyncTransactions()

        pending.forEach { localTransaction ->
            try {
                val serverTransaction = apiCallHelper.safeApiCall({
                    localTransaction.id.takeIf { it > 0 }?.let { apiService.getTransactionDetails(it) }
                })

                when {
                    localTransaction.isDeleted -> handleDeletedTransaction(localTransaction)
                    localTransaction.id < 0 -> handleNewTransaction(localTransaction)
                    else -> handleExistingTransaction(localTransaction, serverTransaction)
                }
            } catch (e: Exception) {
                Log.e("Sync", "Error syncing transaction ${localTransaction.id}", e)
            }
        }
    }

    private suspend fun handleDeletedTransaction(localTransaction: TransactionEntity) {
        val response = apiService.deleteTransaction(localTransaction.id)
        if (response.isSuccessful) {
            transactionDao.deleteTransaction(localTransaction.id)
        }
    }

    private suspend fun handleNewTransaction(localTransaction: TransactionEntity) {
        val request = CreateTransactionRequest(
            accountId = localTransaction.accountId,
            categoryId = localTransaction.categoryId,
            amount = localTransaction.amount,
            transactionDate = localTransaction.transactionDate,
            comment = localTransaction.comment
        )

        when (val response = apiCallHelper.safeApiCall({ apiService.createTransaction(request) })) {
            is ApiResult.Success -> {
                transactionDao.deleteTransaction(localTransaction.id)
                transactionDao.insertTransaction(response.data.toEntity().copy(
                    pendingSync = false
                ))
            }
            else -> {
                transactionDao.updateTransaction(localTransaction.copy(
                    pendingSync = true
                ))
            }
        }
    }

    private suspend fun handleExistingTransaction(
        localTransaction: TransactionEntity,
        serverResult: ApiResult<TransactionResponse?>
    ) {
        when (serverResult) {
            is ApiResult.Success -> {
                val serverTransaction = serverResult.data
                serverTransaction?.let { resolveConflict(localTransaction, it) }
            }
            else -> {
                transactionDao.updateTransaction(localTransaction.copy(
                    pendingSync = true,
                ))
            }
        }
    }

    private suspend fun resolveConflict(
        localTransaction: TransactionEntity,
        serverTransaction: TransactionResponse
    ) {
        val localUpdatedAt = localTransaction.updatedAt ?: localTransaction.createdAt
        val serverUpdatedAt = serverTransaction.updatedAt

        when {
            localUpdatedAt > serverUpdatedAt -> {
                val request = CreateTransactionRequest(
                    accountId = localTransaction.accountId,
                    categoryId = localTransaction.categoryId,
                    amount = localTransaction.amount,
                    transactionDate = localTransaction.transactionDate,
                    comment = localTransaction.comment
                )

                val updated = apiService.updateTransaction(localTransaction.id, request)
                transactionDao.updateTransaction(updated.toEntity().copy(
                    pendingSync = false
                ))
            }

            serverUpdatedAt > localUpdatedAt -> {
                transactionDao.updateTransaction(serverTransaction.toEntity().copy(
                    pendingSync = false
                ))
            }

            else -> {
                transactionDao.updateTransaction(localTransaction.copy(
                    pendingSync = false
                ))
            }
        }
    }

    private fun generateTemporaryId(): Int {
        return -System.currentTimeMillis().toInt()
    }
}

