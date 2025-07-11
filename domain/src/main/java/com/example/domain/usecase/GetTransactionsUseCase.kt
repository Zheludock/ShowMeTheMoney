package com.example.domain.usecase

import com.example.domain.response.ApiResult
import com.example.domain.model.TransactionDomain
import com.example.domain.repository.TransactionRepository
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
    suspend fun execute(
        accountId: Int,
        startDate: String,
        endDate: String
    ): ApiResult<List<TransactionDomain>> {
        return repository.getTransactions(accountId, startDate, endDate)
    }
}