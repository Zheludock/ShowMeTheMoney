package com.example.showmethemoney.data.dto.transaction

data class CreateTransactionRequest(
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    val comment: String? = null
)