package com.example.domain.usecase.transaction

import com.example.domain.model.CategoryStatsDomain
import com.example.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionsAnalysisUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend fun execute(
        accountId: Int,
        startDate: String,
        endDate: String,
        isIncome: Boolean
    ): List<CategoryStatsDomain> {
        val transactions = transactionRepository.getTransactions(accountId, startDate, endDate)
            .filter { it.isIncome == isIncome }

        return transactions
            .groupBy { transaction ->
                Triple(transaction.categoryId, transaction.categoryName, transaction.emoji)
            }
            .map { (categoryInfo, transactionsInCategory) ->
                val totalAmount = transactionsInCategory.sumOf { it.amount.toDouble() }.toString()
                CategoryStatsDomain(
                    categoryId = categoryInfo.first,
                    categoryName = categoryInfo.second,
                    emoji = categoryInfo.third,
                    amount = totalAmount
                )
            }
    }
}
