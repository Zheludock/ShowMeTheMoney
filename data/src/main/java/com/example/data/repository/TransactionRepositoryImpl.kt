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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import java.util.Date
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

    override fun getTransactions(
        accountId: Int,
        startDate: Date,
        endDate: Date
    ): Flow<List<TransactionDomain>> {
        return transactionDao.getTransactions(accountId, startDate, endDate)
            .onStart {
                val count = transactionDao.transactionsCountByAccount(accountId)
                if (count == 0) {
                    val apiResult = apiCallHelper.safeApiCall({
                        apiService.getTransactions(accountId, "2025-06-01", DateUtils.formatDateToBackend(Date()))
                    })
                    if (apiResult is ApiResult.Success) {
                        transactionDao.insertTransactions(apiResult.data.map { it.toEntity() })
                    }
                }
            }
            .map { list ->
                list.filter { !it.transaction.isDeleted }
                    .map { it.toDomain() }
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: Date,
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
            createdAt = Date(),
            updatedAt = null,
            pendingSync = true,
            isDeleted = false
        )
        transactionDao.insertTransaction(newTransaction)
        try {
            val apiResult = apiCallHelper.safeApiCall({
                apiService.createTransaction(
                    CreateTransactionRequest(
                        accountId = AccountManager.selectedAccountId,
                        categoryId = categoryId,
                        amount = amount,
                        transactionDate = DateUtils.dateToIsoString(transactionDate),
                        comment = comment
                    )
                )
            })
            if (apiResult is ApiResult.Success) {
                transactionDao.updateTransaction(
                    newTransaction.copy(
                        id = apiResult.data.id, pendingSync = false
                    )
                )
            }
        } catch (e: Exception) {
            //Ничего не делаем
        }
    }

    override suspend fun getTransactionDetails(transactionId: Int): TransactionDomain {
        val cached = transactionDao.getTransactionWithDetails(transactionId)
        if (cached != null) {
            if (cached.transaction.isDeleted) {
                throw Exception("Transaction $transactionId has been deleted")
            }
            return cached.toDomain()
        }

        val apiResult =
            apiCallHelper.safeApiCall({ apiService.getTransactionDetails(transactionId) })
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
        val newEntity = updatedEntity.copy(
            categoryId = transactionInput.categoryId,
            amount = transactionInput.amount,
            transactionDate = transactionInput.transactionDate,
            comment = transactionInput.comment,
            updatedAt = Date(),
            pendingSync = true,
        )
        transactionDao.updateTransaction(newEntity)
        try {
            val apiResult = apiCallHelper.safeApiCall({apiService.updateTransaction(
                transactionInput.transactionId,
                request = CreateTransactionRequest(
                    accountId = transactionInput.accountId,
                    categoryId = transactionInput.categoryId,
                    amount = transactionInput.amount,
                    transactionDate = DateUtils.dateToIsoString(transactionInput.transactionDate),
                    comment = transactionInput.comment,
                ),
            )})
            if(apiResult is ApiResult.Success){
                transactionDao.updateTransaction(newEntity.copy(pendingSync = false))
            }
        } catch (e: Exception){
            //nothing
        }
    }

    override suspend fun deleteTransaction(transactionId: Int): Boolean {
        transactionDao.markTransactionDeleted(transactionId)
        try {
            val apiResult = apiCallHelper.safeApiCall({ apiService.deleteTransaction(transactionId) })
            if(apiResult is ApiResult.Success){
                transactionDao.deleteTransaction(transactionId)
                return true
            }
        }catch (e: Exception){}
        return transactionDao.getTransactionById(transactionId).isDeleted
    }

    suspend fun syncPendingTransactions() {
        val pending = transactionDao.getPendingSyncTransactions()

        pending.forEach { localTransaction ->
            try {
                val serverTransaction = apiCallHelper.safeApiCall({
                    localTransaction.id.takeIf { it > 0 }
                        ?.let { apiService.getTransactionDetails(it) }
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
            transactionDate = DateUtils.dateToIsoString(localTransaction.transactionDate),
            comment = localTransaction.comment
        )

        when (val response = apiCallHelper.safeApiCall({ apiService.createTransaction(request) })) {
            is ApiResult.Success -> {
                transactionDao.deleteTransaction(localTransaction.id)
                transactionDao.insertTransaction(
                    response.data.toEntity().copy(
                        pendingSync = false
                    )
                )
            }

            else -> {
                transactionDao.updateTransaction(
                    localTransaction.copy(
                        pendingSync = true
                    )
                )
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
                transactionDao.updateTransaction(
                    localTransaction.copy(
                        pendingSync = true,
                    )
                )
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
            localUpdatedAt > serverUpdatedAt?.let { DateUtils.isoStringToDate(it) } -> {
                val request = CreateTransactionRequest(
                    accountId = localTransaction.accountId,
                    categoryId = localTransaction.categoryId,
                    amount = localTransaction.amount,
                    transactionDate = DateUtils.dateToIsoString(localTransaction.transactionDate),
                    comment = localTransaction.comment
                )

                val updated = apiService.updateTransaction(localTransaction.id, request)
                transactionDao.updateTransaction(
                    updated.toEntity().copy(
                        pendingSync = false
                    )
                )
            }

            serverUpdatedAt!! > DateUtils.dateToIsoString(localUpdatedAt) -> {
                transactionDao.updateTransaction(
                    serverTransaction.toEntity().copy(
                        pendingSync = false
                    )
                )
            }

            else -> {
                transactionDao.updateTransaction(
                    localTransaction.copy(
                        pendingSync = false
                    )
                )
            }
        }
    }

    private fun generateTemporaryId(): Int {
        return -System.currentTimeMillis().toInt()
    }
}

