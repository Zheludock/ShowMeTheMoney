package com.example.domain.usecase.transaction

import com.example.domain.model.CategoryStatsDomain
import com.example.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class GetTransactionsAnalysisUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    fun execute(
        accountId: Int,
        startDate: Date,
        endDate: Date,
        isIncome: Boolean
    ): Flow<List<CategoryStatsDomain>> {
        return transactionRepository.getTransactions(accountId, startDate, endDate)
            .map { transactions ->
                transactions.filter { it.isIncome == isIncome }
                    .groupBy { Triple(it.categoryId, it.categoryName, it.emoji) }
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
}
