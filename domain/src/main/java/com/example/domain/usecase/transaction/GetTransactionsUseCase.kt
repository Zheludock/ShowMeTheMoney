package com.example.domain.usecase.transaction

import com.example.domain.model.TransactionDomain
import com.example.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

/**
 * UseCase для получения списка транзакций за указанный период.
 *
 * @param repository репозиторий для работы с транзакциями
 *
 * @param accountId идентификатор аккаунта (преобразуется в Int)
 * @param startDate начальная дата периода в формате строки
 * @param endDate конечная дата периода в формате строки
 *
 * @return ApiResult со списком TransactionDomain в случае успеха
 *         или ошибкой ApiError в случае неудачи
 */
class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    fun execute(
        accountId: Int,
        startDate: Date,
        endDate: Date
    ): Flow<List<TransactionDomain>> {
        return repository.getTransactions(accountId, startDate, endDate)
    }
}