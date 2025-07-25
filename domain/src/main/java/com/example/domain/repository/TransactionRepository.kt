package com.example.domain.repository

import com.example.domain.model.TransactionDomain
import com.example.domain.model.TransactionInput
import kotlinx.coroutines.flow.Flow
import java.util.Date
import kotlin.time.ExperimentalTime

/**
 * Репозиторий для работы с транзакциями.
 * Обеспечивает CRUD операции и дополнительные методы для работы с транзакциями.
 *
 * Основные операции:
 * - Создание новой транзакции
 * - Получение деталей транзакции
 * - Обновление существующей транзакции
 * - Удаление транзакции
 * - Получение списка транзакций с возможностью фильтрации по датам
 *
 * Все методы возвращают ApiResult с соответствующим типом данных.
 */
interface TransactionRepository {
    @OptIn(ExperimentalTime::class)
    fun getTransactions(
        accountId: Int,
        startDate: Date,
        endDate: Date
    ): Flow<List<TransactionDomain>>

    @OptIn(ExperimentalTime::class)
    suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: Date,
        comment: String?
    )

    suspend fun getTransactionDetails(transactionId: Int): TransactionDomain

    suspend fun updateTransaction(transactionInput: TransactionInput)

    suspend fun deleteTransaction(transactionId: Int): Boolean
}