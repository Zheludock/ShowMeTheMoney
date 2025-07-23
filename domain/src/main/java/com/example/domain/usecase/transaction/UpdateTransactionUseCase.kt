package com.example.domain.usecase.transaction

import android.util.Log
import com.example.domain.model.TransactionInput
import com.example.domain.repository.TransactionRepository
import java.util.Date
import javax.inject.Inject

class UpdateTransactionUseCase @Inject constructor(private val repository: TransactionRepository) {
    suspend fun execute(
        id: Int,
        accountId: Int,
        categoryId: Int,
        amount: String,
        date: Date,
        comment: String?
    ) {
        Log.d("EDITTRANSACTION", "Юзкейз получил команду")
        val transactionInput = TransactionInput(
            transactionId = id,
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            transactionDate = date,
            comment = comment
        )
        return repository.updateTransaction(transactionInput)
    }
}