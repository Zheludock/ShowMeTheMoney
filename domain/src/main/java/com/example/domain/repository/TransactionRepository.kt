package com.example.domain.repository

import com.example.domain.model.CreateTransactionDomain
import com.example.domain.model.TransactionDomain
import com.example.domain.model.TransactionInput

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
    suspend fun getTransactions(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): List<TransactionDomain>

    suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): CreateTransactionDomain

    suspend fun getTransactionDetails(transactionId: Int): TransactionDomain

    suspend fun updateTransaction(transactionInput: TransactionInput): TransactionDomain

    suspend fun deleteTransaction(transactionId: Int): Boolean
}