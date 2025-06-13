package com.example.showmethemoney.domain

data class Income(
    val id: String,
    val accountId: String,
    val categoryId: String,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
)
