package com.example.showmethemoney.domain

data class ExpensesDomain(
    val id: String,
    val accountId: String,
    val categoryId: String,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
)
