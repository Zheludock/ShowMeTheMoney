package com.example.domain.model

data class TransactionInput(
    val transactionId: Int,
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    val comment: String? = null
)