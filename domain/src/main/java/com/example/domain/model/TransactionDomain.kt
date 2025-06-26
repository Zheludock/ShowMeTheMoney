package com.example.domain.model

data class TransactionDomain(
    val id: String,
    val emoji: String,
    val categoryName: String,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val currency: String,
    val createdAt: String,
    val updatedAt: String,
    val isIncome: Boolean
)
