package com.example.domain.model

import java.util.Date

data class CreateTransactionDomain(
    val id: Int,
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val transactionDate: Date,
    val comment: String?,
    val createdAt: Date,
    val updatedAt: Date?
)