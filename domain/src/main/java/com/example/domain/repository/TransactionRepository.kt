package com.example.domain.repository

import com.example.domain.response.ApiResult
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
    /**
     * Создает новую транзакцию
     * @param accountId ID аккаунта
     * @param categoryId ID категории
     * @param amount Сумма транзакции
     * @param transactionDate Дата транзакции
     * @param comment Комментарий к транзакции
     */
    suspend fun createTransaction(
        accountId: Int,
        categoryId: String,
        amount: String,
        transactionDate: String,
        comment: String? = null
    ): ApiResult<TransactionDomain>
    /**
     * Получает детали транзакции по ID
     * @param transactionId ID транзакции
     */
    suspend fun getTransactionDetails(transactionId: Int): ApiResult<TransactionDomain>
    /**
     * Обновляет существующую транзакцию
     * @param transactionInput Данные для обновления
     */
    suspend fun updateTransaction(transactionInput: TransactionInput): ApiResult<TransactionDomain>
    /**
     * Удаляет транзакцию
     * @param transactionId ID транзакции
     * @return Результат операции (true - успешно)
     */
    suspend fun deleteTransaction(transactionId: Int): ApiResult<Boolean>
    /**
     * Получает список транзакций с возможностью фильтрации по дате
     * @param accountId ID аккаунта
     * @param startDate Начальная дата периода (опционально)
     * @param endDate Конечная дата периода (опционально)
     */
    suspend fun getTransactions(
        accountId: Int,
        startDate: String? = null,
        endDate: String? = null
    ): ApiResult<List<TransactionDomain>>
}