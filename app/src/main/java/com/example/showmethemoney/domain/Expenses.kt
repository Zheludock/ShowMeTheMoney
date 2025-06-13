package com.example.showmethemoney.domain

data class Expenses(
    val id: String,
    val accountId: String,
    val categoryId: String,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
)
